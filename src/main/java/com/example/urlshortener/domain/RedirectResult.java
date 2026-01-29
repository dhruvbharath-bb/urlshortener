package com.example.urlshortener.domain;
//This is a wrapper for Status and UrlMapping


public class RedirectResult {
    private final RedirectStatus redirectStatus;
    private final UrlMapping urlMapping;


    public RedirectResult(RedirectStatus redirectStatus, UrlMapping urlMapping) {
        this.redirectStatus = redirectStatus;
        this.urlMapping = urlMapping;
    }

    public RedirectStatus getRedirectStatus() {
        return redirectStatus;
    }

    public UrlMapping getUrlMapping() {
        return urlMapping;
    }

    public static RedirectResult found(UrlMapping urlMapping) {
        return new RedirectResult(RedirectStatus.FOUND, urlMapping);
    }

    public static RedirectResult notFound() {
        return new RedirectResult(RedirectStatus.NOT_FOUND, null);
    }

    public static RedirectResult expired() {
        return new RedirectResult(RedirectStatus.EXPIRED, null);
    }
}
