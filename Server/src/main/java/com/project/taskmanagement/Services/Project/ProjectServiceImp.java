package com.project.taskmanagement.Services.Project;

import com.project.taskmanagement.Entities.Project;
import com.project.taskmanagement.Entities.User;
import com.project.taskmanagement.payload.Map.ProjectMapper;
import com.project.taskmanagement.payload.request.ProjectDto;
import com.project.taskmanagement.payload.response.MessageResponse;
import com.project.taskmanagement.repository.Auth.UserRepository;
import com.project.taskmanagement.repository.Project.ProjectRepository;
import com.project.taskmanagement.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImp implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public ResponseEntity<MessageResponse> addProject(ProjectDto projectDto) {
        // Vérification si un projet avec le même nom existe déjà
        if (projectRepository.existsByProjectName(projectDto.getProjectName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("ProjectName already exists"));
        }

        // Récupérer l'utilisateur authentifié depuis le contexte de sécurité
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        Optional<User> existingUser = userRepository.findById(userId);
        if (!existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("User not found"));
        }

        LocalDateTime creationDate = LocalDateTime.now();
        Project project = projectMapper.toEntity(projectDto);
        project.setCreationDate(creationDate);
        project.setAdmin(existingUser.get());
        projectRepository.save(project);

        return ResponseEntity.ok(new MessageResponse("Project created successfully!"));
    }

    @Override
    public ResponseEntity<MessageResponse> updateProject(ProjectDto projectDto, Long id) {
        // Vérification si un autre projet avec le même nom existe déjà
        if (projectRepository.existsByProjectNameAndProjectIdNot(projectDto.getProjectName(), id)) {
            return ResponseEntity.badRequest().body(new MessageResponse("ProjectName already exists"));
        }

        Optional<Project> p = projectRepository.findById(id);
        if (p.isPresent()) {
            Project project = p.get();
            project.setProjectName(projectDto.getProjectName());
            project.setDescription(projectDto.getDescription());
            projectRepository.saveAndFlush(project);
            return ResponseEntity.ok(new MessageResponse("Project updated successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Project not found"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> deleteProject(Long id) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if (projectOptional.isPresent()) {
            projectRepository.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("Project deleted successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Project not found"));
        }
    }

    @Override
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectDto> projectDtos = projectMapper.toDtoList(projects);
        return ResponseEntity.ok(projectDtos);
    }


    @Override
    public ResponseEntity<ProjectDto> getProjectById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        return project.map(p -> ResponseEntity.ok(projectMapper.toDto(p)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<List<ProjectDto>> getProjectsByUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Project> projects = projectRepository.findByAdmin(user.get());
            List<ProjectDto> projectDtos = projectMapper.toDtoList(projects);
            return ResponseEntity.ok(projectDtos);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }
}
