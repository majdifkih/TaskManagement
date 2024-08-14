package com.project.taskmanagement.payload.Map;

import com.project.taskmanagement.Entities.Comment;
import com.project.taskmanagement.payload.request.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "commentId", target = "commentId")
    @Mapping(source = "userComment.username", target = "userComment")
    CommentDto toDto(Comment comment);

    @Mapping(source = "commentId", target = "commentId")
    @Mapping(source = "userComment", target = "userComment.username")
    Comment toEntity(CommentDto commentDto);

    List<CommentDto> toDtoList(List<Comment> comments);
}
