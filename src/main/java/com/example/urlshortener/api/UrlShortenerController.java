package com.example.urlshortener.api;

import com.example.urlshortener.api.dto.CreateShortUrlRequest;
import com.example.urlshortener.api.dto.CreateShortUrlResponse;
import com.example.urlshortener.domain.RedirectResult;
import com.example.urlshortener.domain.RedirectStatus;
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
        RedirectResult redirectResult = urlShortenerService.resolveRedirect(shortCode);
        if(redirectResult.getRedirectStatus()== RedirectStatus.NOT_FOUND){
            return ResponseEntity.notFound().build();
        } else if (redirectResult.getRedirectStatus()==RedirectStatus.EXPIRED) {
            return ResponseEntity.status(HttpStatus.GONE).build();
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectResult.getUrlMapping().getLongUrl()))
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
