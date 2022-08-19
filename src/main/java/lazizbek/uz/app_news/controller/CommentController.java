package lazizbek.uz.app_news.controller;


import lazizbek.uz.app_news.aop.CheckPermission;
import lazizbek.uz.app_news.entity.Comment;
import lazizbek.uz.app_news.payload.ApiResponse;
import lazizbek.uz.app_news.payload.CommentDto;
import lazizbek.uz.app_news.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    CommentService commentService;


    @CheckPermission(permission = "ADD_COMMENT")
    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody CommentDto commentDto) {
        ApiResponse apiResponse = commentService.add(commentDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @CheckPermission(permission = "DELETE_MY_COMMENT")
    @DeleteMapping("/deleteMyComment/{id}")
    public ResponseEntity<?> deleteMyComment(@PathVariable Long id) {
        ApiResponse apiResponse = commentService.deleteMyComment(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @CheckPermission(permission = "DELETE_COMMENT")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        ApiResponse apiResponse = commentService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @GetMapping
    public ResponseEntity<?> get() {
        List<Comment> comments = commentService.get();
        return ResponseEntity.status(comments.size() != 0 ? 200 : 409).body(comments);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Comment comment = commentService.getById(id);
        return ResponseEntity.status(comment != null ? 200 : 409).body(comment);
    }


    @CheckPermission(permission = "EDIT_COMMENT")
    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable Long id, @Valid @RequestBody CommentDto commentDto) {
        ApiResponse apiResponse = commentService.edit(id, commentDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}

