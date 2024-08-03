package com.project.taskmanagement.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    private String content;
    private LocalDateTime creationDate;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
    public Comment() {
    }

    public Comment(String content, LocalDateTime creationDate, Task task) {
        this.content = content;
        this.creationDate = creationDate;
        this.task=task;
    }

}
