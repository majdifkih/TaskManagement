package com.project.taskmanagement.Services.Sprint;

import com.project.taskmanagement.Entities.Project;
import com.project.taskmanagement.Entities.Sprint;
import com.project.taskmanagement.payload.Map.SprintMapper;
import com.project.taskmanagement.payload.request.SprintDto;
import com.project.taskmanagement.repository.Project.ProjectRepository;
import com.project.taskmanagement.repository.Sprint.SprintRepository;
import com.project.taskmanagement.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SprintServiceImp implements SprintService {

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SprintMapper sprintMapper;

    @Override
    public ResponseEntity<MessageResponse> addSprint(SprintDto sprintDto) {
        Optional<Project> project = projectRepository.findById(sprintDto.getProjectId());

        if (project.isPresent()) {
            Sprint sprint = sprintMapper.toEntity(sprintDto);
            sprint.setProject(project.get());

            sprintRepository.save(sprint);
            return ResponseEntity.ok(new MessageResponse("Sprint created successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Project not found"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> updateSprint(Long sprintId, SprintDto sprintDto) {
        Optional<Sprint> sprint = sprintRepository.findById(sprintId);
        if (sprint.isPresent()) {
            Sprint existingSprint = sprint.get();
            sprintMapper.updateEntityFromDto(sprintDto, existingSprint);

            sprintRepository.saveAndFlush(existingSprint);
            return ResponseEntity.ok(new MessageResponse("Sprint updated successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Sprint not found"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> deleteSprint(Long sprintId) {
        Optional<Sprint> sprint = sprintRepository.findById(sprintId);
        if (sprint.isPresent()) {
            sprintRepository.delete(sprint.get());
            return ResponseEntity.ok(new MessageResponse("Sprint deleted successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Sprint not found"));
        }
    }

    @Override
    public ResponseEntity<List<SprintDto>> getAllSprints() {
        List<Sprint> sprints = sprintRepository.findAll();
        List<SprintDto> sprintDtos = sprintMapper.toDtoList(sprints);
        return ResponseEntity.ok(sprintDtos);
    }

    @Override
    public ResponseEntity<SprintDto> getSprintById(Long sprintId) {
        Optional<Sprint> sprint = sprintRepository.findById(sprintId);
        if (sprint.isPresent()) {
            SprintDto sprintDto = sprintMapper.toDto(sprint.get());
            return ResponseEntity.ok(sprintDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Override
    public ResponseEntity<List<SprintDto>> getSprintsByProject(Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent()) {
            List<Sprint> sprints = sprintRepository.findByProject(project.get());
            List<SprintDto> sprintDtos = sprintMapper.toDtoList(sprints);
            return ResponseEntity.ok(sprintDtos);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }
    }

    @Override
    public ResponseEntity<List<SprintDto>> searchSprints(String sprintName, LocalDate endDate, String status) {
        List<Sprint> sprints = sprintRepository.searchSprints(sprintName, endDate, status);
        List<SprintDto> sprintDtos = sprintMapper.toDtoList(sprints);

        if (sprintDtos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        return ResponseEntity.ok(sprintDtos);
    }
}

