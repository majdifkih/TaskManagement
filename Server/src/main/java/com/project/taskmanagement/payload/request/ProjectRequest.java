package com.project.taskmanagement.payload.request;

import com.project.taskmanagement.Entities.User;

import java.time.LocalDateTime;
import java.util.Date;

public class ProjectRequest {
    private String projectName;
    private LocalDateTime creationDate;
    private String description;
    private Long admin;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAdmin() {
        return admin;
    }

    public void setAdmin(Long admin) {
        this.admin = admin;
    }
}
