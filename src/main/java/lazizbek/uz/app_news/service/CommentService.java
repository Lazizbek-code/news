package lazizbek.uz.app_news.service;

import lazizbek.uz.app_news.entity.Comment;
import lazizbek.uz.app_news.entity.Post;
import lazizbek.uz.app_news.entity.User;
import lazizbek.uz.app_news.payload.ApiResponse;
import lazizbek.uz.app_news.payload.CommentDto;
import lazizbek.uz.app_news.repository.CommentRepository;
import lazizbek.uz.app_news.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;


    // add
    public ApiResponse add(CommentDto commentDto) {
        Optional<Post> optionalPost = postRepository.findById(commentDto.getPostId());
        if (!optionalPost.isPresent()) {
            return new ApiResponse("Post not found", false);
        }
        Post post = optionalPost.get();

        Comment comment = new Comment(commentDto.getText(), post);
        commentRepository.save(comment);
        return new ApiResponse("Successfully added", true);
    }


    // delete my comment
    public ApiResponse deleteMyComment(Long id) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Comment> optionalComment = commentRepository.findById(id);
            if (optionalComment.isPresent()) {
                Comment comment = optionalComment.get();
                if (comment.getCreatedBy().equals(user.getId())) {
                    commentRepository.deleteById(id);
                }
            }
            return new ApiResponse("Successfully deleted", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }


    // get all
    public List<Comment> get() {
        return commentRepository.findAll();
    }


    // get by id
    public Comment getById(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.orElse(null);
    }


    // edit
    public ApiResponse edit(Long id, CommentDto commentDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (!optionalComment.isPresent()) {
            return new ApiResponse("Comment not found", false);
        }
        Comment comment = optionalComment.get();

        if (comment.getCreatedBy().equals(user.getId())) {
            comment.setText(commentDto.getText());
            commentRepository.save(comment);
            return new ApiResponse("Comment successfully edited", true);
        }
        return new ApiResponse("You can not edit this comment", false);
    }


    // delete
    public ApiResponse delete(Long id) {
        try {
            commentRepository.deleteById(id);
            return new ApiResponse("Deleted", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }
}
