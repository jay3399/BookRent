package com.example.bookrent.util;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;


    public void setValueWithExp(String key, String value , long timeout , TimeUnit timeUnit) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, timeout, timeUnit);

    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().getAndDelete(key);
    }


}
