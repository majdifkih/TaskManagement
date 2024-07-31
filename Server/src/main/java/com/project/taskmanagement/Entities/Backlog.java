//package com.project.taskmanagement.Entities;
//
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import jakarta.persistence.*;
//import org.antlr.v4.runtime.misc.NotNull;
//
//import java.util.List;
//import com.fasterxml.jackson.annotation.JsonBackReference;
//
//@Entity
//public class Backlog {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long backlogId;
//    private String description;
//
//    @OneToOne
//    @JoinColumn(name = "project_id")
//    @JsonBackReference
//    private Project project;
//
//    @OneToMany(mappedBy = "backlog", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private List<Sprint> sprints;
//
//    public Backlog() {
//    }
//
//    public Backlog(String description, Project project) {
//        this.description = description;
//        this.project = project;
//    }
//
//    public Long getBacklogId() {
//        return backlogId;
//    }
//
//    public void setBacklogId(Long backlogId) {
//        this.backlogId = backlogId;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public Project getProject() {
//        return project;
//    }
//
//    public void setProject(Project project) {
//        this.project = project;
//    }
//
//    public List<Sprint> getSprints() {
//        return sprints;
//    }
//
//    public void setSprints(List<Sprint> sprints) {
//        this.sprints = sprints;
//    }
//}
