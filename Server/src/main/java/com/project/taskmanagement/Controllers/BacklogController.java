//package com.project.taskmanagement.Controllers;
//
//
//import com.project.taskmanagement.Services.Backlog.BacklogService;
//import com.project.taskmanagement.payload.request.BacklogRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//
//@RestController
//@RequestMapping("/user/backlogs")
//public class BacklogController {
//
//    @Autowired
//    private BacklogService backlogService;
//
//    @PostMapping("/addbacklog")
//    public ResponseEntity<?> addBacklog(@RequestBody BacklogRequest backlogRequest) {
//        return backlogService.addBacklog(backlogRequest);
//    }
//
//    @PutMapping("/updatebacklog/{id}")
//    public ResponseEntity<?> updateBacklog(@RequestBody BacklogRequest backlogRequest, @PathVariable Long id) {
//        return backlogService.updateBacklog(backlogRequest, id);
//    }
//
//  //  @DeleteMapping("/delbacklog/{id}")
//    //public ResponseEntity<?> deleteBacklog(@PathVariable Long id) {
//      //  return backlogService.deleteBacklog(id);
//    //}
//
//
//
//    @GetMapping("/backlog/{projectId}")
//    public ResponseEntity<?> getBacklogsByProject(@PathVariable Long projectId) {
//        return backlogService.getBacklogByProject(projectId);
//    }
//}