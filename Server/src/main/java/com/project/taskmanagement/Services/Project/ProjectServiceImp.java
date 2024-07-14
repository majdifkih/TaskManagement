package com.project.taskmanagement.Services.Project;

import com.project.taskmanagement.Entities.Project;
import com.project.taskmanagement.payload.request.ProjectRequest;
import com.project.taskmanagement.payload.response.MessageResponse;
import com.project.taskmanagement.repository.Project.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImp implements ProjectService{
    @Autowired
    ProjectRepository projectRepository;
    @Override
    public ResponseEntity<?> addProject(ProjectRequest projectRequest){

        Project project = new Project(projectRequest.getProjectName());

        projectRepository.save(project);

        return ResponseEntity.ok(new MessageResponse("Project created successfully!"));
    }
    @Override
    public ResponseEntity<?> updateProject(ProjectRequest projectRequest, Long id) {
        Optional<Project> p = projectRepository.findById(id);

        if (p.isPresent()) {
            Project project = p.get();
            project.setProjectName(projectRequest.getProjectName());

            projectRepository.saveAndFlush(project);

            return ResponseEntity.ok(new MessageResponse("Project updated successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Project not found"));
        }
    }

    @Override
    public ResponseEntity<?> deleteProject(Long id) {
        Optional<Project> projectOptional = projectRepository.findById(id);

        if (projectOptional.isPresent()) {
            projectRepository.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("Project deleted successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Project not found"));
        }
    }

    @Override
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return ResponseEntity.ok(projects);
    }

    @Override
    public ResponseEntity<Project> getProjectById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        return project.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
