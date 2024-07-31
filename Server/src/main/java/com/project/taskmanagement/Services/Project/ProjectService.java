package com.project.taskmanagement.Services.Project;

import com.project.taskmanagement.Entities.Project;
import com.project.taskmanagement.payload.request.ProjectRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProjectService {
    public ResponseEntity<?> addProject(ProjectRequest projectRequest);
    public ResponseEntity<?> updateProject(ProjectRequest projectRequest, Long id);
    public ResponseEntity<?> deleteProject(Long id);
    public ResponseEntity<List<Project>> getAllProjects();
    public ResponseEntity<Project> getProjectById(Long id);
    public ResponseEntity<?> getProjectsByUser(Long userId);
}
