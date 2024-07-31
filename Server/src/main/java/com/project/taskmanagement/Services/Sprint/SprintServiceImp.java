package com.project.taskmanagement.Services.Sprint;

import com.project.taskmanagement.Entities.Project;
import com.project.taskmanagement.Entities.Sprint;
import com.project.taskmanagement.payload.request.SprintRequest;
import com.project.taskmanagement.repository.Project.ProjectRepository;
import com.project.taskmanagement.repository.Sprint.SprintRepository;
import com.project.taskmanagement.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SprintServiceImp implements SprintService {

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public ResponseEntity<?> addSprint(SprintRequest sprintRequest) {
        try {
            Optional<Project> project = projectRepository.findById(sprintRequest.getProject());

            if (project.isPresent()) {
                Sprint sprint = new Sprint();
                sprint.setSprintName(sprintRequest.getSprintName());
                sprint.setSprintDescription(sprintRequest.getSprintDescription());
                sprint.setStatus(sprintRequest.getStatus());
                sprint.setPriority(sprintRequest.getPriority());
                sprint.setStartDate(sprintRequest.getStartDate());
                sprint.setEndDate(sprintRequest.getEndDate());
                sprint.setProject(project.get());

                sprintRepository.save(sprint);
                return ResponseEntity.ok(new MessageResponse("Sprint created successfully!"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Project not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while creating the Sprint: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> updateSprint(Long sprintId, SprintRequest sprintRequest) {
        try {
            Optional<Sprint> sprint = sprintRepository.findById(sprintId);
            if (sprint.isPresent()) {
                Sprint existingSprint = sprint.get();
                existingSprint.setSprintName(sprintRequest.getSprintName());
                existingSprint.setSprintDescription(sprintRequest.getSprintDescription());
                existingSprint.setStatus(sprintRequest.getStatus());
                existingSprint.setPriority(sprintRequest.getPriority());
                existingSprint.setStartDate(sprintRequest.getStartDate());
                existingSprint.setEndDate(sprintRequest.getEndDate());

                sprintRepository.saveAndFlush(existingSprint);
                return ResponseEntity.ok(new MessageResponse("Sprint updated successfully!"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Sprint not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while updating the Sprint: " + e.getMessage()));
        }
    }
    @Override
    public ResponseEntity<?> deleteSprint(Long sprintId) {
        try {
            Optional<Sprint> sprint = sprintRepository.findById(sprintId);
            if (sprint.isPresent()) {
                sprintRepository.delete(sprint.get());
                return ResponseEntity.ok(new MessageResponse("Sprint deleted successfully!"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Sprint not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while deleting the Sprint: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> getAllSprints() {
        try {
            List<Sprint> sprints = sprintRepository.findAll();
            return ResponseEntity.ok(sprints);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while retrieving the sprints: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> getSprintById(Long sprintId) {
        try {
            Optional<Sprint> sprint = sprintRepository.findById(sprintId);
            if (sprint.isPresent()) {
                return ResponseEntity.ok(sprint.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Sprint not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while retrieving the Sprint: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> getSprintsByProject(Long projectId) {
        try {
            Optional<Project> project = projectRepository.findById(projectId);
            if (project.isPresent()) {
                List<Sprint> sprints = sprintRepository.findByProject(project.get());
                return ResponseEntity.ok(sprints);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Project not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while retrieving the sprints: " + e.getMessage()));
        }
    }
    @Override
    public ResponseEntity<?> searchSprints(String sprintName, LocalDate endDate, String status) {
        try {
            List<Sprint> sprints = sprintRepository.searchSprints(sprintName, endDate, status);

            if (sprints.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Aucun sprint trouvé"));
            }

            return ResponseEntity.ok(sprints);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Une erreur est survenue lors de la recherche des sprints : " + e.getMessage()));
        }
    }


}
