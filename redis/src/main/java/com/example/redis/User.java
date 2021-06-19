package com.example.redis;

import lombok.Builder;

public class User {

    private Long id;
    private String username;
    private int level;

    @Builder
    public User(Long id, String username, int level) {
        this.id = id;
        this.username = username;
        this.level = level;
    }
}
