package com.project.taskmanagement.repository.Sprint;

import com.project.taskmanagement.Entities.Project;
import com.project.taskmanagement.Entities.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface SprintRepository extends JpaRepository<Sprint,Long> {
    List<Sprint> findByProject(Project prject);
    @Query("SELECT s FROM Sprint s WHERE " +
            "(:sprintName IS NULL OR LOWER(s.sprintName) LIKE LOWER(CONCAT('%', :sprintName, '%'))) AND " +
            "(:endDate IS NULL OR s.endDate = :endDate) AND " +
            "(:status IS NULL OR LOWER(s.status) = LOWER(:status))")
    List<Sprint> searchSprints(@Param("sprintName") String sprintName,
                               @Param("endDate") LocalDate endDate,
                               @Param("status") String status);

}
