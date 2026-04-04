package com.example.indexquiz.common.config.cache;

import com.github.benmanes.caffeine.cache.Expiry;
import java.time.*;

public class MidNightExpiry<K, V> implements Expiry<K, V> {

    @Override
    public long expireAfterCreate(K key, V value, long currentTime) {
        return nanosUntilNextMidnight();
    }

    @Override
    public long expireAfterUpdate(K key, V value, long currentTime, long currentDuration) {
        return nanosUntilNextMidnight();
    }

    @Override
    public long expireAfterRead(K key, V value, long currentTime, long currentDuration) {
        return currentDuration;
    }

    private long nanosUntilNextMidnight() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        ZonedDateTime nextMidnight = now.plusDays(1).toLocalDate().atStartOfDay(now.getZone());
        return Duration.between(now, nextMidnight).toNanos();
    }
}

