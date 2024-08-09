package com.project.taskmanagement.Controllers;

import com.project.taskmanagement.Services.Sprint.SprintService;
import com.project.taskmanagement.payload.request.SprintDto;
import com.project.taskmanagement.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user/sprints")
public class SprintController {

    @Autowired
    private SprintService sprintService;
    @GetMapping("/search")
    public ResponseEntity<List<SprintDto>> searchSprints(@RequestParam Long projectId,
            @RequestParam(required = false) String sprintName,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String status) {
        return sprintService.searchSprints(projectId,sprintName, endDate, status);
    }

    @PostMapping("/addsprint")
    public ResponseEntity<MessageResponse> addSprint(@RequestBody SprintDto sprintDto) {
        return sprintService.addSprint(sprintDto);
    }

    @PutMapping("/updatesprint/{id}")
    public ResponseEntity<MessageResponse> updateSprint(@PathVariable Long id, @RequestBody SprintDto sprintDto) {
        return sprintService.updateSprint(id, sprintDto);
    }

    @DeleteMapping("/delsprint/{id}")
    public ResponseEntity<MessageResponse> deleteSprint(@PathVariable Long id) {
        return sprintService.deleteSprint(id);
    }

    @GetMapping("/allsprint")
    public ResponseEntity<List<SprintDto>> getAllSprints() {
        return sprintService.getAllSprints();
    }

    @GetMapping("/sprint/{id}")
    public ResponseEntity<SprintDto> getSprintById(@PathVariable Long id) {
        return sprintService.getSprintById(id);
    }

    @GetMapping("/sprintbacklog/{projectId}")
    public ResponseEntity<List<SprintDto>> getSprintsByProject(@PathVariable Long projectId) {
        return sprintService.getSprintsByProject(projectId);
    }
    @PostMapping("/order")
    public ResponseEntity<MessageResponse> updateSprintOrder(@RequestBody List<SprintDto> sprintsDto) {
        return sprintService.updateSprintOrder(sprintsDto);
    }
}
