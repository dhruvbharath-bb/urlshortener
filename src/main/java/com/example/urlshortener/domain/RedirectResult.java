package com.example.urlshortener.domain;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RedirectResult {

    private final RedirectStatus redirectStatus;
    // Exactly one of these may be non-null when status == FOUND
    private final UrlMapping urlMapping; // DB-backed path
    private final String longUrl;        // Cache-backed path

    // Private constructor to enforce valid states
    private RedirectResult(
            RedirectStatus redirectStatus,
            UrlMapping urlMapping,
            String longUrl
    ) {
        this.redirectStatus = redirectStatus;
        this.urlMapping = urlMapping;
        this.longUrl = longUrl;
    }

    public RedirectStatus getRedirectStatus() {
        return redirectStatus;
    }
    //Important - if it misses cache and hit from DB, get LongUrl from UrlMapping
    public String getLongUrl() {
        if (urlMapping != null) {
            return urlMapping.getLongUrl();
        }
        return longUrl;
    }
    //can be null for cache-redirects
    public UrlMapping getUrlMapping() {
        return urlMapping;
    }


    //DB-result
    public static RedirectResult found(UrlMapping urlMapping) {
        return new RedirectResult(RedirectStatus.FOUND, urlMapping, null);
    }

    //Cache-result
    public static RedirectResult found(String longUrl) {
        return new RedirectResult(RedirectStatus.FOUND, null, longUrl);
    }

    public static RedirectResult notFound() {
        return new RedirectResult(RedirectStatus.NOT_FOUND, null, null);
    }

    public static RedirectResult expired() {
        return new RedirectResult(RedirectStatus.EXPIRED, null, null);
    }
}