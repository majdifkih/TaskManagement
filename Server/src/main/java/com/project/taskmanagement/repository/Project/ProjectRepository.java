package com.project.taskmanagement.repository.Project;

import com.project.taskmanagement.Entities.Project;
import com.project.taskmanagement.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByAdmin(User admin);
    boolean existsByProjectNameAndAdminIdAndProjectId(String projectName, Long userId, Long id);
   boolean existsByProjectNameAndAdminId(String projectName, Long userId);
}
