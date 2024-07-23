package com.project.taskmanagement.payload.request;

import com.project.taskmanagement.Entities.Project;
import com.project.taskmanagement.Entities.Sprint;
import java.util.List;

public class BacklogRequest {
    private Long backlogId;
    private String description;
    private Long project_id;

    public Long getBacklogId() {
        return backlogId;
    }

    public void setBacklogId(Long backlogId) {
        this.backlogId = backlogId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getProject() {
        return project_id;
    }

    public void setProject(Long project) {
        this.project_id = project;
    }
}
