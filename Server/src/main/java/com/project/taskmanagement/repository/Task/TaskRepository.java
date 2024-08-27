package com.project.taskmanagement.repository.Task;

import com.project.taskmanagement.Entities.Sprint;
import com.project.taskmanagement.Entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findBySprint(Sprint sprint);
    List<Task> findByUsers_Id(Long userId);

    @Query("SELECT p.projectName FROM Project p " +
            "JOIN p.sprints s " +
            "JOIN s.tasks t " +
            "WHERE t.taskId = :taskId")
    String findProjectNameByTaskId(@Param("taskId") Long taskId);

    @Query("SELECT t.status, COUNT(t.taskId) " +
            "FROM Task t " +
            "LEFT JOIN t.users u " +
            "LEFT JOIN t.sprint s " +
            "LEFT JOIN s.project p " +
            "WHERE u.id = :userId OR p.admin.id = :userId " +
            "GROUP BY t.status")
    List<Object[]> countTasksByStatusForUser(@Param("userId") Long userId);
}
