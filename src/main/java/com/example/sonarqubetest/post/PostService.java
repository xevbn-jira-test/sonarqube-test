package com.example.sonarqubetest.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public PostResponse create(PostRequest request) {
        Post post = new Post(request.title(), request.content(), request.author());
        return PostResponse.from(postRepository.save(post));
    }
}
