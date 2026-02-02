package com.example.urlshortener.infrastructure.redis;

import com.example.urlshortener.cache.CachedRedirect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {
    @Bean
    @ConditionalOnProperty(name = "spring.data.redis.enabled", havingValue = "true", matchIfMissing = true)
    public RedisConnectionFactory redisConnectionFactory(RedisProperties properties){
        return new LettuceConnectionFactory(properties.getHost(),properties.getPort());

    }
    @Bean
    public RedisTemplate<String, CachedRedirect> redisTemplate(
            RedisConnectionFactory connectionFactory
    ) {
        RedisTemplate<String, CachedRedirect> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        return template;
    }
}
