package com.project.taskmanagement.Controllers;

import com.project.taskmanagement.Services.Project.ProjectService;
import com.project.taskmanagement.payload.request.ProjectDto;
import com.project.taskmanagement.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/allproject")
    public ResponseEntity<List<ProjectDto>> getAllProjects() {

            return projectService.getAllProjects();
    }

    @GetMapping("/projectdetail/{id}")
    public ResponseEntity<ProjectDto> getProjectDetail(@PathVariable Long id) {
            return projectService.getProjectById(id);
    }
    @GetMapping("/project/{userId}")
    public ResponseEntity<List<ProjectDto>> getProjectByUser(@PathVariable Long userId) {
        return projectService.getProjectsByUser(userId);
    }

    @PostMapping("/addproject")
    public ResponseEntity<MessageResponse> addProject(@RequestBody ProjectDto projectDto) {

            return projectService.addProject(projectDto);

    }

    @PutMapping("/updateproject/{id}")
    public ResponseEntity<MessageResponse> updateProject(@RequestBody ProjectDto projectDto, @PathVariable Long id) {

            return projectService.updateProject(projectDto, id);
    }

    @DeleteMapping("/delproject/{id}")
    public ResponseEntity<MessageResponse> deleteProject(@PathVariable Long id) {
            return projectService.deleteProject(id);

    }

    @GetMapping("/userprojects")
    public ResponseEntity<List<ProjectDto>> getProjectsForUser() {
        List<ProjectDto> projects = projectService.getProjectsForUser();
        return ResponseEntity.ok(projects);
    }
}
