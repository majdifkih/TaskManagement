package com.project.taskmanagement.repository.Task;

import com.project.taskmanagement.Entities.Sprint;
import com.project.taskmanagement.Entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findBySprint(Sprint sprint);
}
