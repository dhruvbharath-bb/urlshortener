package com.example.urlshortener.api;

import com.example.urlshortener.api.dto.CreateShortUrlRequest;
import com.example.urlshortener.api.dto.CreateShortUrlResponse;
import com.example.urlshortener.domain.UrlMapping;
import com.example.urlshortener.service.UrlShortenerServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UrlShortenerController {
    private final UrlShortenerServiceImpl urlShortenerService;

    public UrlShortenerController(UrlShortenerServiceImpl urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode){
        Optional<UrlMapping> optionalMapping = urlShortenerService.getLongUrl(shortCode);
        if(optionalMapping.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        UrlMapping urlMapping = optionalMapping.get();

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(urlMapping.getLongUrl()))
                .build();
    }

    @PostMapping("/shorten")
    public CreateShortUrlResponse createShortUrl(@RequestBody CreateShortUrlRequest request){
        String shortCode;
        if(request.getExpiryDays()==null) {
            shortCode = urlShortenerService.createShortUrl(request.getLongUrl());
        }else {
            shortCode = urlShortenerService.createShortUrl(request.getLongUrl(), request.getExpiryDays());
        }
        String shortUrl = "http://localhost:8080/api/v1/" + shortCode;
        return new CreateShortUrlResponse(shortUrl);
    }
}
