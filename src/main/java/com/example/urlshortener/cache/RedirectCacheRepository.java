package com.example.urlshortener.cache;

import java.util.Optional;

public interface RedirectCacheRepository {
    Optional<CachedRedirect> get(String shortCode);
    void put(String shortCode, CachedRedirect redirect, long ttlSeconds);
}
