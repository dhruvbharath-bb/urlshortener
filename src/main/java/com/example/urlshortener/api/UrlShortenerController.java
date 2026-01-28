package com.example.urlshortener.api;

import com.example.urlshortener.api.dto.CreateShortUrlRequest;
import com.example.urlshortener.api.dto.CreateShortUrlResponse;
import com.example.urlshortener.service.UrlShortenerServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UrlShortenerController {
    private final UrlShortenerServiceImpl urlShortenerService;

    public UrlShortenerController(UrlShortenerServiceImpl urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }
    @PostMapping("/shorten")
    public CreateShortUrlResponse createShortUrl(@RequestBody CreateShortUrlRequest request){
        String shortCode;
        if(request.getExpiryDays()==null) {
            shortCode = urlShortenerService.createShortUrl(request.getLongUrl());
        }else {
            shortCode = urlShortenerService.createShortUrl(request.getLongUrl(), request.getExpiryDays());
        }
        String shortUrl = "http://localhost:8080/" + shortCode;
        return new CreateShortUrlResponse(shortUrl);
    }
}
