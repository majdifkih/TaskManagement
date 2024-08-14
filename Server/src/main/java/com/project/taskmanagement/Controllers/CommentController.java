package com.project.taskmanagement.Controllers;

import com.project.taskmanagement.Services.Comment.CommentService;
import com.project.taskmanagement.payload.request.CommentDto;
import com.project.taskmanagement.payload.request.ProjectDto;
import com.project.taskmanagement.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/comments")
public class CommentController {
    @Autowired
    CommentService commentService;
    @PostMapping("/addcomment/{taskId}")
    public ResponseEntity<MessageResponse> addProject(@PathVariable Long taskId, @RequestBody CommentDto commentDto) {

        return commentService.addComment(taskId,commentDto);

    }
    @PutMapping("/updatecomment/{commentId}")
    public ResponseEntity<MessageResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentDto commentDto) {

        return commentService.updateComment(commentDto,commentId);

    }
    @DeleteMapping("/delcomment/{commentId}")
    public ResponseEntity<MessageResponse> delComment(@PathVariable Long commentId) {

        return commentService.deleteComment(commentId);
     }
    @GetMapping("/allcomment/{taskId}")
    public ResponseEntity<List<CommentDto>> allcomments(@PathVariable Long taskId) {

        return commentService.getAllCommentsByTask(taskId);

    }
}
