package com.project.taskmanagement.repository.Auth;

import com.project.taskmanagement.Entities.Role;
import com.project.taskmanagement.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  List<User> findByRole(Role role);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}
