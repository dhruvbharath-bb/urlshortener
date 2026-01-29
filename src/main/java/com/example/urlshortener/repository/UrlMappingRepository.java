package com.example.urlshortener.repository;

import com.example.urlshortener.domain.UrlMapping;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UrlMappingRepository extends MongoRepository<UrlMapping,String> {
    Optional<UrlMapping> findByShortCode(String shortCode);
}
