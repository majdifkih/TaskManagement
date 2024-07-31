package com.project.taskmanagement.Controllers;

import com.project.taskmanagement.Services.Sprint.SprintService;
import com.project.taskmanagement.payload.request.SprintRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;

@RestController
@RequestMapping("/user/sprints")
public class SprintController {

    @Autowired
    private SprintService sprintService;
    @GetMapping("/search")
    public ResponseEntity<?> searchSprints(
            @RequestParam(required = false) String sprintName,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String status) {
        return sprintService.searchSprints(sprintName, endDate, status);
    }

    @PostMapping("/addsprint")
    public ResponseEntity<?> addSprint(@RequestBody SprintRequest sprintRequest) {
        return sprintService.addSprint(sprintRequest);
    }

    @PutMapping("/updatesprint/{id}")
    public ResponseEntity<?> updateSprint(@PathVariable Long id, @RequestBody SprintRequest sprintRequest) {
        return sprintService.updateSprint(id, sprintRequest);
    }

    @DeleteMapping("/delsprint/{id}")
    public ResponseEntity<?> deleteSprint(@PathVariable Long id) {
        return sprintService.deleteSprint(id);
    }

    @GetMapping("/allsprint")
    public ResponseEntity<?> getAllSprints() {
        return sprintService.getAllSprints();
    }

    @GetMapping("/sprint/{id}")
    public ResponseEntity<?> getSprintById(@PathVariable Long id) {
        return sprintService.getSprintById(id);
    }

    @GetMapping("/Sprintbacklog/{projectId}")
    public ResponseEntity<?> getSprintsByBacklog(@PathVariable Long projectId) {
        return sprintService.getSprintsByProject(projectId);
    }
}
