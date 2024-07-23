package com.project.taskmanagement.Services.Sprint;

import com.project.taskmanagement.payload.request.SprintRequest;
import org.springframework.http.ResponseEntity;

public interface SprintService {
    ResponseEntity<?> addSprint(SprintRequest sprintRequest);
    ResponseEntity<?> updateSprint(Long sprintId, SprintRequest sprintRequest);
    ResponseEntity<?> deleteSprint(Long sprintId);
    ResponseEntity<?> getAllSprints();
    ResponseEntity<?> getSprintById(Long sprintId);
    ResponseEntity<?> getSprintsByBacklog(Long backlogId);
}

