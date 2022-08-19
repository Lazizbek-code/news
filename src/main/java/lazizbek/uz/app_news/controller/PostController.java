package lazizbek.uz.app_news.controller;


import lazizbek.uz.app_news.aop.CheckPermission;
import lazizbek.uz.app_news.entity.Post;
import lazizbek.uz.app_news.payload.ApiResponse;
import lazizbek.uz.app_news.payload.PostDto;
import lazizbek.uz.app_news.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    PostService postService;


    @CheckPermission(permission = "ADD_POST")
    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody PostDto postDto) {
        ApiResponse apiResponse = postService.add(postDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @CheckPermission(permission = "DELETE_POST")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        ApiResponse apiResponse = postService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @GetMapping
    public ResponseEntity<?> get() {
        List<Post> posts = postService.get();
        return ResponseEntity.status(posts.size() != 0 ? 200 : 409).body(posts);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Post post = postService.getById(id);
        return ResponseEntity.status(post != null ? 200 : 409).body(post);
    }


    @CheckPermission(permission = "EDIT_POST")
    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable Long id, @Valid @RequestBody PostDto postDto) {
        ApiResponse apiResponse = postService.edit(id, postDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}

