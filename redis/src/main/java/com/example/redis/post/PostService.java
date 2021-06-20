package com.example.redis.post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;

    public ResponsePost getPost(Long id) {
        Post post = postRepository.findById(id).orElse(null);

        return ResponsePost.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdDate(post.getCreatedDate())
                .build();
    }

    public List<ResponsePost> getPosts() {
        List<Post> posts = postRepository.findAll();

        return posts.stream().map(post -> ResponsePost.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdDate(post.getCreatedDate())
                .build()).collect(Collectors.toList());
    }

    public ResponsePost updatePost(Long id, RequestPost requestPost) {
        Post post = postRepository.findById(id).orElse(null);
        post.updatePost(requestPost.getTitle(), requestPost.getContent());

        return ResponsePost.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdDate(post.getCreatedDate())
                .build();
    }

    public ResponsePost savePost(RequestPost requestPost) {
        Post post = Post.builder()
                .title(requestPost.getTitle())
                .content(requestPost.getContent())
                .build();

        Post savedPost = postRepository.save(post);

        return ResponsePost.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .createdDate(savedPost.getCreatedDate())
                .build();
    }

    public Long deletePost(Long id) {
        postRepository.deleteById(id);
        return id;
    }
}
