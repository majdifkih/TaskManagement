package com.project.taskmanagement.Services.Comment;

import com.project.taskmanagement.payload.request.CommentDto;
import com.project.taskmanagement.payload.response.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentService {
    ResponseEntity<MessageResponse> addComment(Long taskId,CommentDto commentDto);
    ResponseEntity<MessageResponse> updateComment(CommentDto commentDto, Long id);
    ResponseEntity<MessageResponse> deleteComment(Long id);
    ResponseEntity<List<CommentDto>> getAllCommentsByTask(Long taskId);
}
