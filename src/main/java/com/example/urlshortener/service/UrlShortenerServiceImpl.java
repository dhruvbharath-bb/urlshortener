package com.example.urlshortener.service;

import com.example.urlshortener.cache.CachedRedirect;
import com.example.urlshortener.cache.RedirectCacheRepository;
import com.example.urlshortener.domain.RedirectResult;
import com.example.urlshortener.domain.UrlMapping;
import com.example.urlshortener.repository.UrlMappingRepository;
import com.example.urlshortener.util.ShortCodeEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService{
    private final UrlMappingRepository urlRepository;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final RedirectCacheRepository cacheRepository;
    private static final Logger log = LoggerFactory.getLogger(UrlShortenerServiceImpl.class);


    public UrlShortenerServiceImpl(UrlMappingRepository urlRepository, SequenceGeneratorService sequenceGeneratorService, RedirectCacheRepository cacheRepository) {
        this.urlRepository = urlRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.cacheRepository = cacheRepository;
    }

    @Override
    public String createShortUrl(String longUrl, Integer expiryDays){
        if (longUrl == null || longUrl.isBlank()){
            throw new IllegalArgumentException("Valid Long-URL must be provided");
        }
        Instant currentTime = Instant.now();
        Instant expiresAt = null;
        if(expiryDays!=null){
            if(expiryDays < 0){
                throw new IllegalArgumentException("Enter a positive value for Expiry Days");
            }
            expiresAt = currentTime.plus(expiryDays, ChronoUnit.DAYS);//Adds expiryDays to expiresAt
        }
        long generatedId = sequenceGeneratorService.generateSequence("url_mapping");
        String shortCode = ShortCodeEncoder.encodeId(generatedId);
        UrlMapping urlMapping = new UrlMapping(shortCode,
                longUrl,
                currentTime,
                expiresAt);
        urlRepository.save(urlMapping);
        return shortCode;
    }

    @Override
    public String createShortUrl(String longUrl){
        return createShortUrl(longUrl,null);
    }

    //Will use Read-through redis here
    @Override
    public RedirectResult resolveRedirect(String shortCode) {

        if (shortCode == null || shortCode.isBlank()) {
            return RedirectResult.notFound();
        }

        // Try cache first
        try {
            Optional<CachedRedirect> cached = cacheRepository.get(shortCode);

            if (cached.isPresent()) {
                log.debug("Redirect cache HIT for shortCode={}", shortCode);

                CachedRedirect value = cached.get();

                if (value.getExpiresAt() != null
                        && value.getExpiresAt().isBefore(Instant.now())) {
                    return RedirectResult.expired();
                }

                return RedirectResult.found(value.getLongUrl());
            }
        } catch (Exception e) {
            log.warn("Redirect cache READ failed for shortCode={}", shortCode, e);
        }

        log.debug("Redirect cache MISS for shortCode={}", shortCode);

        // Fallback to MongoDB
        Optional<UrlMapping> optionalMapping =
                urlRepository.findByShortCode(shortCode);

        if (optionalMapping.isEmpty()) {
            return RedirectResult.notFound();
        }

        UrlMapping mapping = optionalMapping.get();

        if (mapping.getExpiresAt() != null
                && mapping.getExpiresAt().isBefore(Instant.now())) {
            return RedirectResult.expired();
        }

        //Cache population
        try {
            long ttlSeconds = calculateTtlSeconds(mapping);

            cacheRepository.put(
                    shortCode,
                    new CachedRedirect(mapping.getLongUrl(), mapping.getExpiresAt()),
                    ttlSeconds
            );

            log.debug(
                    "Redirect cached for shortCode={} (ttl={}s)",
                    shortCode,
                    ttlSeconds
            );
        } catch (Exception e) {
            log.warn("Redirect cache WRITE failed for shortCode={}", shortCode, e);
        }

        return RedirectResult.found(mapping);
    }

    // TTL calculation helper
    private long calculateTtlSeconds(UrlMapping mapping) {

        // No expiry → cache for a bounded time
        if (mapping.getExpiresAt() == null) {
            return Duration.ofHours(24).getSeconds();
        }

        long seconds =
                Duration.between(Instant.now(), mapping.getExpiresAt())
                        .getSeconds();

        // Safety: never return zero or negative TTL
        return Math.max(seconds, 1);
    }
}
