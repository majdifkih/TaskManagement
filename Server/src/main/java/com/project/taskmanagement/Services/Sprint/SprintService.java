package com.project.taskmanagement.Services.Sprint;

import com.project.taskmanagement.payload.request.SprintDto;
import com.project.taskmanagement.payload.response.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface SprintService {
    ResponseEntity<MessageResponse> addSprint(SprintDto sprintDto);
    ResponseEntity<MessageResponse> updateSprint(Long sprintId, SprintDto sprintDto);
    ResponseEntity<MessageResponse> deleteSprint(Long sprintId);
    ResponseEntity<List<SprintDto>> getAllSprints();
    ResponseEntity<SprintDto> getSprintById(Long sprintId);
    ResponseEntity<List<SprintDto>> getSprintsByProject(Long projectId);
    ResponseEntity<List<SprintDto>> searchSprints(String sprintName, LocalDate endDate, String status);
}

