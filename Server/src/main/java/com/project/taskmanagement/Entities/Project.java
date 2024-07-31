package com.project.taskmanagement.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long project_id;

    private String projectName;

    private LocalDateTime creationDate;

    private String description;

    @OneToMany(mappedBy = "sprintId", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Sprint> sprints;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User admin;

    public Project() {
    }

    public Project(String projectName, LocalDateTime creationDate, String description, User admin) {
        this.projectName = projectName;
        this.creationDate = creationDate;
        this.description = description;
        this.admin = admin;
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

    public List<Sprint> getSprints() {
        return sprints;
    }

    public void setSprints(List<Sprint> sprints) {
        this.sprints = sprints;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
