package com.project.taskmanagement.Services.Project;

import com.project.taskmanagement.payload.request.ProjectDto;
import com.project.taskmanagement.payload.response.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProjectService {
    public ResponseEntity<MessageResponse> addProject(ProjectDto projectDto);
    public ResponseEntity<MessageResponse> updateProject(ProjectDto projectDto, Long id);
    public ResponseEntity<MessageResponse> deleteProject(Long id);
    public ResponseEntity<List<ProjectDto>> getAllProjects();
    public ResponseEntity<ProjectDto> getProjectById(Long id);
    public ResponseEntity<List<ProjectDto>> getProjectsByUser(Long userId);
}
