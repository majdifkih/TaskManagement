package com.project.taskmanagement.repository.Sprint;

import com.project.taskmanagement.Entities.Backlog;
import com.project.taskmanagement.Entities.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprintRepository extends JpaRepository<Sprint,Long> {
    List<Sprint> findByBacklog(Backlog backlog);
}
