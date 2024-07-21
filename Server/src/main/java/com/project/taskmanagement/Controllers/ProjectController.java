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
@RequestMapping("/admin/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/allproject")
    public ResponseEntity<List<Project>> getAllProjects() {

            return projectService.getAllProjects();

    }

    @GetMapping("/projectdetail/{id}")
    public ResponseEntity<Project> getProjectDetail(@PathVariable Long id) {
            return projectService.getProjectById(id);

    }

    @PostMapping("/addproject")
    public ResponseEntity<?> addProject(@RequestBody ProjectRequest projectRequest) {

            return projectService.addProject(projectRequest);

    }

    @PutMapping("/updateproject/{id}")
    public ResponseEntity<?> updateProject(@RequestBody ProjectRequest projectRequest, @PathVariable Long id) {

            return projectService.updateProject(projectRequest, id);
    }

    @DeleteMapping("/delproject/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
            return projectService.deleteProject(id);

    }
}
