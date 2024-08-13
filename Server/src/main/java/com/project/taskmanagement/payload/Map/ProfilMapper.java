package com.project.taskmanagement.payload.Map;

import com.project.taskmanagement.Entities.User;
import com.project.taskmanagement.payload.request.ProfilDto;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
@Mapper(componentModel = "spring")
public interface ProfilMapper {

    ProfilDto toDto(User user);


    User toEntity(ProfilDto profilDto);


    void updateEntityFromDto(ProfilDto profilDto, @MappingTarget User user);
    @Mapping(source = "id", target = "id")
    List<ProfilDto> toDtoList(List<User> users);

    List<User> toEntityList(List<ProfilDto> profilDtos);
}
