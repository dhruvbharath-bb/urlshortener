package com.example.urlshortener.service;

public interface UrlShortenerService {
    String createShortUrl(String longUrl, Integer expiryDays);
    String createShortUrl(String longUrl);

}
