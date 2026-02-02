package com.example.urlshortener.infrastructure.redis;

import com.example.urlshortener.cache.CachedRedirect;
import com.example.urlshortener.cache.RedirectCacheRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
public class RedisRedirectCacheRepository implements RedirectCacheRepository {

    private final RedisTemplate<String, CachedRedirect> redisTemplate;

    public RedisRedirectCacheRepository(
            RedisTemplate<String, CachedRedirect> redisTemplate
    ) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Optional<CachedRedirect> get(String shortCode) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(key(shortCode))
        );
    }

    @Override
    public void put(String shortCode, CachedRedirect redirect, long ttlSeconds) {
        redisTemplate.opsForValue().set(
                key(shortCode),
                redirect,
                Duration.ofSeconds(ttlSeconds)
        );
    }

    private String key(String shortCode) {
        return "redirect:" + shortCode;
    }
}