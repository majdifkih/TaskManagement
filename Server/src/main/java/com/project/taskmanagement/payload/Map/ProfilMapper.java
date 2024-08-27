package com.project.taskmanagement.payload.Map;

import com.project.taskmanagement.Entities.User;
import com.project.taskmanagement.payload.request.ProfilDto;
import org.mapstruct.*;

import java.util.List;
@Mapper(componentModel = "spring")
public interface ProfilMapper {
    @Named("defaultToDto")
    @Mapping(target = "oldPassword", ignore = true)
    @Mapping(target = "newPassword", ignore = true)
    ProfilDto toDto(User user);

    @Named("toDtoWithoutPasswords")
    default ProfilDto toDtoWithoutPasswords(User user) {
        ProfilDto profilDto = toDto(user);
        profilDto.setOldPassword(null);
        profilDto.setNewPassword(null);
        return profilDto;
    }

    User toEntity(ProfilDto profilDto);

    void updateEntityFromDto(ProfilDto profilDto, @MappingTarget User user);

    @IterableMapping(qualifiedByName = "defaultToDto")
    List<ProfilDto> toDtoList(List<User> users);

    List<User> toEntityList(List<ProfilDto> profilDtos);
}
