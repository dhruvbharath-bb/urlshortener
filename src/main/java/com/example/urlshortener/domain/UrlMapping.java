package com.example.urlshortener.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@Document(collection="url_mapping")
public class UrlMapping {
    @Id
    private String id;

    @Indexed(unique = true)
    private String shortCode;

    private String longUrl;
    private Instant createdAt;
    private Instant expiresAt; //Can be null

    protected UrlMapping(){
        //for MongoDB-SpringData
    }

    public UrlMapping(String shortCode, String longUrl, Instant createdAt, Instant expiresAt) {
        if(shortCode==null||longUrl==null||createdAt==null){
            throw new IllegalArgumentException("Mandatory fields cannot be null");
        }

        this.shortCode = shortCode;
        this.longUrl = longUrl;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public UrlMapping(String shortCode, String longUrl, Instant createdAt){
        this(shortCode, longUrl, createdAt, null);
    }

    public String getId() {
        return id;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}