package com.project.taskmanagement.payload.Map;

import com.project.taskmanagement.Entities.Sprint;
import com.project.taskmanagement.Entities.Task;
import com.project.taskmanagement.payload.request.SprintDto;

import com.project.taskmanagement.payload.request.TaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


import java.util.List;

@Mapper(componentModel = "spring")
public interface SprintMapper {
    @Mapping(source = "project.projectId", target = "projectId")
    SprintDto toDto(Sprint sprint);

    @Mapping(source = "projectId", target = "project.projectId")
    Sprint toEntity(SprintDto sprintDto);

    @Mapping(target = "sprintId", ignore = true)
    void updateEntityFromDto(SprintDto sprintDto, @MappingTarget Sprint sprint);

    List<SprintDto> toDtoList(List<Sprint> sprints);

    List<Sprint> toEntityList(List<SprintDto> sprintDtos);
}

