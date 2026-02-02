package com.example.urlshortener.cache;

import java.io.Serializable;
import java.time.Instant;

public class CachedRedirect implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String longUrl;
    private final Instant expiresAt;

    public CachedRedirect(String longUrl, Instant expiresAt) {
        this.longUrl = longUrl;
        this.expiresAt = expiresAt;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}