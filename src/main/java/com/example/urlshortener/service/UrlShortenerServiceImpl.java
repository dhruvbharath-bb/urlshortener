package com.example.urlshortener.service;

import com.example.urlshortener.domain.UrlMapping;
import com.example.urlshortener.repository.UrlMappingRepository;
import com.example.urlshortener.util.ShortCodeEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService{
    private final UrlMappingRepository urlRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    public UrlShortenerServiceImpl(UrlMappingRepository urlRepository, SequenceGeneratorService sequenceGeneratorService) {
        this.urlRepository = urlRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
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

    @Override
    public Optional<UrlMapping> getLongUrl(String shortCode){
        if (shortCode ==null||shortCode.isBlank()){
            return Optional.empty();
        }
        Optional<UrlMapping> optionalMapping = urlRepository.findByShortCode(shortCode);
        if (optionalMapping.isEmpty()){
            return Optional.empty();
        }
        UrlMapping urlMapping = optionalMapping.get();

        if(urlMapping.getExpiresAt()!=null && urlMapping.getExpiresAt().isBefore(Instant.now())){
            return Optional.empty(); //for now will handle it later
        }
        return Optional.of(urlMapping);

    }

}
