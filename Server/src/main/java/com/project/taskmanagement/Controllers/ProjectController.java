package com.project.taskmanagement.Controllers;

import com.project.taskmanagement.Entities.Project;
import com.project.taskmanagement.Services.Project.ProjectService;
import com.project.taskmanagement.payload.request.ProjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/allproject")
    public ResponseEntity<List<Project>> getAllProjects() {
        try {
            return projectService.getAllProjects();
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/projectdetail/{id}")
    public ResponseEntity<Project> getProjectDetail(@PathVariable Long id) {
        try {
            return projectService.getProjectById(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/addproject")
    public ResponseEntity<?> addProject(@RequestBody ProjectRequest projectRequest) {
        try {
            return projectService.addProject(projectRequest);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/updateproject/{id}")
    public ResponseEntity<?> updateProject(@RequestBody ProjectRequest projectRequest, @PathVariable Long id) {
        try {
            return projectService.updateProject(projectRequest, id);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delproject/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        try {
            return projectService.deleteProject(id);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
