package com.project.taskmanagement.payload.request;

import java.time.LocalDateTime;
import java.util.Date;

public class ProjectRequest {
    private String projectName;
    private LocalDateTime creationDate;
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
}
