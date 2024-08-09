package com.project.taskmanagement.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class UsersTaskDto {
    private Long taskId;
    private List<Long> userIds;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
