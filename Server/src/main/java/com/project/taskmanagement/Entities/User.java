package com.project.taskmanagement.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	private String username;


	private String email;


	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;
	@ManyToMany(mappedBy = "users")
	private Set<Task> tasks;
	@OneToMany(mappedBy = "projectId", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Project> projects;

	@OneToMany(mappedBy = "commentId", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Comment> comments;
	public User() {
	}

	public User(String username, String email, String password, Role role) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
}
