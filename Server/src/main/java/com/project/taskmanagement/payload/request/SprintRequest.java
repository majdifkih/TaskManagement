package com.project.taskmanagement.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.taskmanagement.Entities.Task;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class SprintRequest {
    private String sprintName;
    private String sprintDescription;
    private String status;
    private Integer priority;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime creationDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private Long backlog;

    public String getSprintName() {
        return sprintName;
    }

    public void setSprintName(String sprintName) {
        this.sprintName = sprintName;
    }

    public String getSprintDescription() {
        return sprintDescription;
    }

    public void setSprintDescription(String sprintDescription) {
        this.sprintDescription = sprintDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getBacklog() {
        return backlog;
    }

    public void setBacklog(Long backlog) {
        this.backlog = backlog;
    }


}
