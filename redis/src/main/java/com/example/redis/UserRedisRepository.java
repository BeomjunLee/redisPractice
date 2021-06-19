package com.example.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRedisRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public void test() {
        ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();

    }
}
