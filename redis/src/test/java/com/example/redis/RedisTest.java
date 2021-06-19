package com.example.redis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class RedisTest {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Test
    @DisplayName("string 테스트")
    public void string() throws Exception{
        //given
        ValueOperations<String, Object> string = redisTemplate.opsForValue();

        //when
        string.set("user:username", "beomjun");
        string.set("user:level", "1");

        Object username = string.get("user:username");
        Object level = string.get("user:level");

        //then
        assertThat(username).isEqualTo("beomjun");
        assertThat(level).isEqualTo("1");
    }

    @Test
    @DisplayName("sorted set 테스트")
    public void sortedSet() throws Exception{
        //given
        String key = "userRank";
        ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();

        //when
        zSet.add(key, "d", 4);
        zSet.add(key, "c", 3);
        zSet.add(key, "a", 1);
        zSet.add(key, "b", 2);
        zSet.add(key, "f", 6);
        zSet.add(key, "e", 5);
        Set<Object> range = zSet.range(key, 0, 6);

        Set<Object> expect = new HashSet<>();
        expect.add("a");
        expect.add("b");
        expect.add("c");
        expect.add("d");
        expect.add("e");
        expect.add("f");

        //then
        assertThat(range.size()).isEqualTo(6);
        assertThat(range).usingRecursiveComparison().isEqualTo(expect);
    }
}
