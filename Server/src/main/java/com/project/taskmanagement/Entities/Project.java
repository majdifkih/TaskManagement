package com.project.taskmanagement.Entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long project_id;
    private String projectName;
    private LocalDateTime creationDate;

    @OneToOne(mappedBy = "project")
    @JsonManagedReference
    private Backlog backlog;

    public Project() {
    }

    public Project(String projectName, LocalDateTime creationDate) {
        this.projectName = projectName;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return project_id;
    }

    public void setId(Long id) {
        this.project_id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Backlog getBacklog() {
        return backlog;
    }

    public void setBacklog(Backlog backlog) {
        this.backlog = backlog;
    }
}
