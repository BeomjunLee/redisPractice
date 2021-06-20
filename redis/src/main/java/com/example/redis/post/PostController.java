package com.example.redis.post;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;


    @PostMapping
    public ResponsePost writePost(@RequestBody RequestPost requestPost) {
        return postService.savePost(requestPost);
    }

    @Cacheable(key = "#id", value = "post")
    @GetMapping("/{id}")
    public ResponsePost getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @GetMapping
    public List<ResponsePost> getPosts() {
        return postService.getPosts();
    }

    @CachePut(key = "#id", value = "post")
    @PutMapping("/{id}")
    public ResponsePost updatePost(@PathVariable Long id,
                                   @RequestBody RequestPost requestPost) {
        return postService.updatePost(id, requestPost);
    }

    @CacheEvict(key = "#id", value = "post")
    @DeleteMapping("/{id}")
    public ResponseDeletePost deletePost(@PathVariable Long id) {
        Long deletedId = postService.deletePost(id);

        return ResponseDeletePost.builder()
                .id(deletedId)
                .message("삭제 성공")
                .build();
    }

    @Getter
    static class ResponseDeletePost {
        private Long id;
        private String message;

        @Builder
        public ResponseDeletePost(Long id, String message) {
            this.id = id;
            this.message = message;
        }
    }
}
