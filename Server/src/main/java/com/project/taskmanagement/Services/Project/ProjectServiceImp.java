package com.project.taskmanagement.Services.Project;

import com.project.taskmanagement.Entities.Project;
import com.project.taskmanagement.payload.request.ProjectRequest;
import com.project.taskmanagement.payload.response.MessageResponse;
import com.project.taskmanagement.repository.Project.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImp implements ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    public ResponseEntity<?> addProject(@RequestBody ProjectRequest projectRequest) {
        try {
            LocalDateTime creationDate = LocalDateTime.now();
            Project project = new Project(projectRequest.getProjectName(), creationDate);
            projectRepository.save(project);
            return ResponseEntity.ok(new MessageResponse("Project created successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while creating the project: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> updateProject(ProjectRequest projectRequest, Long id) {
        try {
            Optional<Project> p = projectRepository.findById(id);
            if (p.isPresent()) {
                Project project = p.get();
                project.setProjectName(projectRequest.getProjectName());
                projectRepository.saveAndFlush(project);
                return ResponseEntity.ok(new MessageResponse("Project updated successfully!"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Project not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while updating the project: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> deleteProject(Long id) {
        try {
            Optional<Project> projectOptional = projectRepository.findById(id);
            if (projectOptional.isPresent()) {
                projectRepository.deleteById(id);
                return ResponseEntity.ok(new MessageResponse("Project deleted successfully!"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Project not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while deleting the project: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<List<Project>> getAllProjects() {
        try {
            List<Project> projects = projectRepository.findAll();
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @Override
    public ResponseEntity<Project> getProjectById(Long id) {
        try {
            Optional<Project> project = projectRepository.findById(id);
            return project.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
