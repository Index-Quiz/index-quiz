package com.example.indexquiz.common.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {

    private static final String CACHE_NAME = "difficultQuestionCache";

    @Bean
    public CacheManager difficultQuestionCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(CACHE_NAME);
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(10_000)
                        .expireAfter(new MidNightExpiry<>()) // 다음날 자정까지
                        .recordStats()
        );
        return cacheManager;
    }
}
