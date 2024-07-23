package com.project.taskmanagement.Controllers;

import com.project.taskmanagement.Services.Sprint.SprintService;
import com.project.taskmanagement.payload.request.SprintRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/sprints")
public class SprintController {

    @Autowired
    private SprintService sprintService;

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

    @GetMapping("/Sprintbacklog/{backlogId}")
    public ResponseEntity<?> getSprintsByBacklog(@PathVariable Long backlogId) {
        return sprintService.getSprintsByBacklog(backlogId);
    }
}
