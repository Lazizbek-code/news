package lazizbek.uz.app_news.service;

import lazizbek.uz.app_news.entity.Post;
import lazizbek.uz.app_news.payload.ApiResponse;
import lazizbek.uz.app_news.payload.PostDto;
import lazizbek.uz.app_news.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;


    public ApiResponse add(PostDto postDto) {
        Post post = new Post(postDto.getTitle(), postDto.getText(), postDto.getUrl());
        postRepository.save(post);
        return new ApiResponse("Successfully added", true);
    }


    public ApiResponse delete(Long id) {
        try {
            postRepository.deleteById(id);
            return new ApiResponse("Successfully deleted", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }


    public List<Post> get() {
        return postRepository.findAll();
    }


    public Post getById(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }


    public ApiResponse edit(Long id, PostDto postDto) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (!optionalPost.isPresent()) {
            return new ApiResponse("Post not found",false);
        }
        Post post = optionalPost.get();
        post.setTitle(postDto.getTitle());
        post.setText(postDto.getText());
        post.setUrl(postDto.getUrl());
        postRepository.save(post);
        return new ApiResponse("Successfully edited", true);
    }
}
