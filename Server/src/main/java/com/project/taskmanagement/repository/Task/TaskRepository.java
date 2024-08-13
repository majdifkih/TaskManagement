package com.project.taskmanagement.repository.Task;

import com.project.taskmanagement.Entities.Sprint;
import com.project.taskmanagement.Entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findBySprint(Sprint sprint);
}
