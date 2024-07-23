package com.project.taskmanagement.Services.Backlog;

import com.project.taskmanagement.Entities.Backlog;
import com.project.taskmanagement.payload.request.BacklogRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BacklogService {
    public ResponseEntity<?> addBacklog(BacklogRequest backlogRequest);
    public ResponseEntity<?> updateBacklog(BacklogRequest backlogRequest, Long id);
    public ResponseEntity<?> deleteBacklog(Long id);
    public ResponseEntity<List<Backlog>> getAllBacklog();
    public ResponseEntity<Backlog> getBacklog(Long id);
    public ResponseEntity<?> getBacklogByProject(Long projectId);
}
