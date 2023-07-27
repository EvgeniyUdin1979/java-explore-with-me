package ru.practicum.comments.util;

import lombok.experimental.UtilityClass;
import ru.practicum.comments.dto.CommentInDto;
import ru.practicum.comments.dto.CommentOutDto;
import ru.practicum.comments.dto.CommentUpdateInDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentStatus;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;
import ru.practicum.users.util.UserMapper;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public Comment mapToEntity(CommentInDto inDto, Event event, User user, Comment parent) {
        Comment.CommentBuilder builder = Comment.builder();
        if (parent != null) {
            builder.parentComment(parent);
        }
        return builder.text(inDto.getText())
                .status(CommentStatus.PENDING)
                .event(event)
                .creator(user)
                .created(LocalDateTime.now())
                .build();
    }

    public CommentOutDto mapToOut(Comment comment) {
        CommentOutDto.CommentOutDtoBuilder builder = CommentOutDto.builder();
        Comment parentComment = comment.getParentComment();
        if (parentComment != null){
                builder.parent_id(parentComment.getId());
        }
        return builder
                .id(comment.getId())
                .text(comment.getText())
                .event_id(comment.getEvent().getId())
                .creator(UserMapper.mapToOutShort(comment.getCreator()))
                .created(comment.getCreated())
                .status(comment.getStatus())
                .build();
    }

    public static Comment mapUpdateToEntity(CommentUpdateInDto inDto, Comment comment) {
        if (inDto.getText() != null) {
            comment.setText(inDto.getText());
        }
        if (inDto.getStatus() != null) {
            comment.setStatus(inDto.getStatus());
        } else if (comment.getStatus() == CommentStatus.PUBLISHED) {
            comment.setStatus(CommentStatus.PENDING);
        }
        return comment;
    }
}
