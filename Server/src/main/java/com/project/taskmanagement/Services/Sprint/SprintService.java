package com.project.taskmanagement.Services.Sprint;

import com.project.taskmanagement.Entities.Sprint;
import com.project.taskmanagement.payload.request.SprintRequest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface SprintService {
    ResponseEntity<?> addSprint(SprintRequest sprintRequest);
    ResponseEntity<?> updateSprint(Long sprintId, SprintRequest sprintRequest);
    ResponseEntity<?> deleteSprint(Long sprintId);
    ResponseEntity<?> getAllSprints();
    ResponseEntity<?> getSprintById(Long sprintId);
    ResponseEntity<?> getSprintsByProject(Long projectId);
    ResponseEntity<?> searchSprints(String sprintName, LocalDate endDate, String status);
}

