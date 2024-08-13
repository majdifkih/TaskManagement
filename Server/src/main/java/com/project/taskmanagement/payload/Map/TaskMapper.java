package com.project.taskmanagement.payload.Map;

import com.project.taskmanagement.Entities.Task;
import com.project.taskmanagement.payload.request.TaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(source = "sprint.sprintId", target = "sprintId")
    TaskDto toDto(Task task);

    @Mapping(source = "sprintId", target = "sprint.sprintId")
    Task toEntity(TaskDto taskDto);


    @Mapping(target = "taskId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "taskOrder", ignore = true)
    @Mapping(target = "sprint", ignore = true)
    void updateEntityFromDto(TaskDto taskDto, @MappingTarget Task task);

    @Mapping(target = "taskId", ignore = true)
    @Mapping(target = "taskName", ignore = true)
    @Mapping(target = "taskDescription", ignore = true)
    @Mapping(target = "priority", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "sprint", ignore = true)
    void updateStatusAndOrderFromDto(TaskDto taskDto, @MappingTarget Task task);
    List<TaskDto> toDtoList(List<Task> tasks);
}
