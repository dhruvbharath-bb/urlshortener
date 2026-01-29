package com.example.urlshortener.service;

import com.example.urlshortener.domain.RedirectResult;
import com.example.urlshortener.domain.UrlMapping;

import java.util.Optional;

public interface UrlShortenerService {
    String createShortUrl(String longUrl, Integer expiryDays);
    String createShortUrl(String longUrl);
    public RedirectResult resolveRedirect(String shortCode);

}
