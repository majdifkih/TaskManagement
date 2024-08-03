package com.project.taskmanagement.payload.Map;

import com.project.taskmanagement.Entities.Project;
import com.project.taskmanagement.payload.request.ProjectDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(source = "projectId", target = "projectId")
    ProjectDto toDto(Project project);

    @Mapping(source = "projectId", target = "projectId")
    Project toEntity(ProjectDto projectDto);

    List<ProjectDto> toDtoList(List<Project> projects);

}
