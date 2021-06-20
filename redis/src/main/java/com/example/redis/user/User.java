package com.example.redis.user;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@RedisHash("user")
public class User {
    @Id
    private String id;
    private int level;
    private LocalDateTime refreshTime;

    @Builder
    public User(String id, int level, LocalDateTime refreshTime) {
        this.id = id;
        this.level = level;
        this.refreshTime = refreshTime;
    }

    public void refresh(int level, LocalDateTime refreshTime) {
        if (refreshTime.isAfter(this.refreshTime)) {
            this.level = level;
            this.refreshTime = refreshTime;
        }
    }
}
