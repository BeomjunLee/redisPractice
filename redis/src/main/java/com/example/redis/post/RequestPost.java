package com.example.redis.post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestPost {
    private String title;
    private String content;

    @Builder
    public RequestPost(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
