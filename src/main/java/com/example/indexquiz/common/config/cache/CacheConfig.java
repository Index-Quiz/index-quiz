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

    private static final String CACHE_NAME = "questionCache";

    @Bean
    public CacheManager difficultquestioncachemanager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(CACHE_NAME);

        long maxWeight = (long) (Runtime.getRuntime().maxMemory() * 0.10); //JVM 최대 힙 메모리 크기의 10%

        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumWeight(maxWeight)
                        .weigher((key, value) -> ((CacheWeighable) value).estimateSize()) //예상된 메모리 용량
                        .expireAfter(new MidNightExpiry<>()) // 다음날 자정까지
                        .recordStats()
        );
        return cacheManager;
    }
}
