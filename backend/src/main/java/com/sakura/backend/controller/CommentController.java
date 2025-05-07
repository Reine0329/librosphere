package com.sakura.backend.controller;

import com.sakura.backend.model.Comment;
import com.sakura.backend.model.User;
import com.sakura.backend.service.CommentService;
import com.sakura.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @GetMapping("/comments")
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/comment/get/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        Optional<Comment> comment = commentService.getCommentById(id);
        return comment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/comment/book/{bookId}")
    public List<Comment> getCommentsByBook(@PathVariable Long bookId) {
        return commentService.getCommentsByBook(bookId);
    }

    @GetMapping("/comment/user/{userId}")
    public List<Comment> getCommentsByUser(@PathVariable Long userId) {
        return commentService.getCommentsByUser(userId);
    }

    @PostMapping("/comment")
    public Comment createComment(@RequestBody Comment comment) {
        User user = userService.getUserByName(comment.getUser().getName()).orElseThrow(() -> new RuntimeException("用户未找到"));
        comment.setUser(user);
        return commentService.createComment(comment);
    }

    @DeleteMapping("/comment/delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
