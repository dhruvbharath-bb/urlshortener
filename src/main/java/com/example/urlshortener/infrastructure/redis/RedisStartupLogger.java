package com.example.urlshortener.infrastructure.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class RedisStartupLogger {
    private static final Logger log = LoggerFactory.getLogger(RedisStartupLogger.class);
    public RedisStartupLogger(Optional<RedisConnectionFactory> factory){
        if(factory.isPresent()){
            log.info("Redis factory has INITIALIZED");
        }else {
            log.warn("Redis is DISABLED OR NOT AVAILABLE");
        }
    }
}
