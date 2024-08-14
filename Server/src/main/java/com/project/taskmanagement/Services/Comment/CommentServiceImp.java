package com.project.taskmanagement.Services.Comment;

import com.project.taskmanagement.Entities.Comment;
import com.project.taskmanagement.Entities.Task;
import com.project.taskmanagement.Entities.User;
import com.project.taskmanagement.payload.Map.CommentMapper;
import com.project.taskmanagement.payload.request.CommentDto;
import com.project.taskmanagement.payload.response.MessageResponse;
import com.project.taskmanagement.repository.Auth.UserRepository;
import com.project.taskmanagement.repository.Comment.CommentRepository;
import com.project.taskmanagement.repository.Task.TaskRepository;
import com.project.taskmanagement.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImp implements CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    CommentMapper commentMapper;
    @Override
    public ResponseEntity<MessageResponse> addComment(Long taskId, CommentDto commentDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        Optional<User> existingUser = userRepository.findById(userId);
        if (!existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("User not found"));
        }

        Optional<Task> existingTask = taskRepository.findById(taskId);
        if (!existingTask.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Task not found"));
        }

        LocalDateTime creationDate = LocalDateTime.now();
        Comment comment = commentMapper.toEntity(commentDto);
        comment.setCreationDate(creationDate);
        comment.setContent(commentDto.getContent());
        comment.setUserComment(existingUser.get());
        comment.setTask(existingTask.get());

        commentRepository.save(comment);

        return ResponseEntity.ok(new MessageResponse("Comment created successfully!"));
    }

    @Override
    public ResponseEntity<MessageResponse> updateComment(CommentDto commentDto, Long id) {

      Optional<Comment> existingComment = commentRepository.findById(id);
        if (!existingComment.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Comment not found"));
        }

        Comment comment = existingComment.get();

        comment.setContent(commentDto.getContent());

        commentRepository.saveAndFlush(comment);
        return ResponseEntity.ok(new MessageResponse("Comment updated successfully!"));
    }

    @Override
    public ResponseEntity<MessageResponse> deleteComment(Long id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            commentRepository.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("Comment deleted successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Comment not found"));
        }
    }


    @Override
    public ResponseEntity<List<CommentDto>> getAllCommentsByTask(Long taskId) {
        List<Comment> comments = commentRepository.findByTask_TaskId(taskId);
        List<CommentDto> commentDtos = commentMapper.toDtoList(comments);
        return ResponseEntity.ok(commentDtos);
    }



}
