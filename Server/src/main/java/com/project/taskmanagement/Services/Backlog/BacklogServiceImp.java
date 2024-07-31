//package com.project.taskmanagement.Services.Backlog;
//
//import com.project.taskmanagement.Entities.Backlog;
//import com.project.taskmanagement.Entities.Project;
//import com.project.taskmanagement.payload.request.BacklogRequest;
//import com.project.taskmanagement.payload.response.MessageResponse;
//import com.project.taskmanagement.repository.Backlog.BacklogRepository;
//import com.project.taskmanagement.repository.Project.ProjectRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class BacklogServiceImp implements BacklogService {
//    @Autowired
//    BacklogRepository backlogRepository;
//
//    @Autowired
//    private ProjectRepository projectRepository;
//
//    public ResponseEntity<?> addBacklog(BacklogRequest backlogRequest) {
//        try {
//            Long projectId = backlogRequest.getProject();
//            if (projectId == null) {
//                return ResponseEntity.badRequest().body(new MessageResponse("Project ID must not be null"));
//            }
//
//
//            Optional<Project> existingProject = projectRepository.findById(projectId);
//            if (!existingProject.isPresent()) {
//                return ResponseEntity.badRequest().body(new MessageResponse("Project not found"));
//            }
//
//            Backlog backlog = new Backlog(backlogRequest.getDescription(), existingProject.get());
//            backlogRepository.save(backlog);
//
//            return ResponseEntity.ok(new MessageResponse("Backlog created successfully!"));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new MessageResponse("An error occurred while creating the Backlog: " + e.getMessage()));
//        }
//    }
//
//    @Override
//    public ResponseEntity<?> updateBacklog(BacklogRequest backlogRequest, Long id) {
//        try {
//            Optional<Backlog> b = backlogRepository.findById(id);
//            if (b.isPresent()) {
//                Backlog backlog = b.get();
//                backlog.setDescription(backlogRequest.getDescription());
//                backlogRepository.saveAndFlush(backlog);
//                return ResponseEntity.ok(new MessageResponse("Backlog updated successfully!"));
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Backlog not found"));
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new MessageResponse("An error occurred while updating the Backlog: " + e.getMessage()));
//        }
//    }
//
//
//
//
//
//    @Override
//    public ResponseEntity<?> getBacklogByProject(Long projectId) {
//        try {
//            Optional<Project> project = projectRepository.findById(projectId);
//            if (project.isPresent()) {
//                List<Backlog> backlogs = backlogRepository.findByProject(project.get());
//                return ResponseEntity.ok(backlogs);
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Project not found"));
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new MessageResponse("An error occurred while retrieving the backlogs: " + e.getMessage()));
//        }
//    }
//}