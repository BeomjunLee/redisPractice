package com.example.redis;

import com.example.redis.user.User;
import com.example.redis.user.UserRedisRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class RedisTest {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    UserRedisRepository userRedisRepository;

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
    @DisplayName("list 테스트")
    public void list() throws Exception{
        //given
        ListOperations<String, Object> list = redisTemplate.opsForList();
        String key = "userList";

        List<Object> expect = new ArrayList<>();
        expect.add("user1");
        expect.add("user2");
        expect.add("user3");
        expect.add("user4");

        //when
        list.rightPush(key, "user2");
        list.rightPush(key, "user3");
        list.rightPush(key, "user4");
        list.leftPush(key, "user1");
        List<Object> range = list.range(key, 0, 3);

        //then
        assertThat(range).usingRecursiveComparison().isEqualTo(expect);
    }

    @Test
    @DisplayName("set 테스트")
    public void set() throws Exception{
        //given
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        String key = "userSet";

        Set<Object> expect = new HashSet<>();
        expect.add("a");
        expect.add("b");
        expect.add("c");

        //when
        set.add(key, "a");
        set.add(key, "a");
        set.add(key, "b");
        set.add(key, "b");
        set.add(key, "c");
        set.add(key, "c");
        Set<Object> members = set.members(key);

        //then
        assertThat(members).usingRecursiveComparison().isEqualTo(expect);
    }

    @Test
    @DisplayName("sorted set 테스트")
    public void sortedSet() throws Exception{
        //given
        String key = "userRank";
        ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();

        Set<Object> expect = new HashSet<>();
        expect.add("a");
        expect.add("b");
        expect.add("c");
        expect.add("d");
        expect.add("e");
        expect.add("f");

        //when
        zSet.add(key, "d", 4);
        zSet.add(key, "c", 3);
        zSet.add(key, "a", 1);
        zSet.add(key, "b", 2);
        zSet.add(key, "f", 6);
        zSet.add(key, "e", 5);
        Set<Object> range = zSet.range(key, 0, 6);

        //then
        assertThat(range.size()).isEqualTo(6);
        assertThat(range).usingRecursiveComparison().isEqualTo(expect);
    }

    @Test
    @DisplayName("hash 테스트")
    public void hash() throws Exception{
        //given
        String key = "beomjun";
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();


        //when
        hash.put(key, "level", "1");
        hash.put(key, "age", "26");
        hash.put(key, "job", "sever");
        Map<Object, Object> entries = hash.entries(key);

        //then
        assertThat(entries.get("level")).isEqualTo("1");
        assertThat(entries.get("age")).isEqualTo("26");
        assertThat(entries.get("job")).isEqualTo("sever");

    }

    @Test
    @DisplayName("jpa redis 테스트")
    public void jpaRedisTest() throws Exception{
        //given
        LocalDateTime refreshTime = LocalDateTime.of(2021, 5, 20, 0, 0);
        User user = User.builder()
                .id("beomjun")
                .level(5)
                .refreshTime(refreshTime)
                .build();
        userRedisRepository.save(user);

        //when
        User findUser = userRedisRepository.findById(user.getId()).get();
        findUser.refresh(10, LocalDateTime.of(2021, 6, 20, 0, 0));
        userRedisRepository.save(findUser);

        //then
        User refreshUser = userRedisRepository.findById(user.getId()).get();
        assertThat(refreshUser.getLevel()).isEqualTo(10);
    }
}
