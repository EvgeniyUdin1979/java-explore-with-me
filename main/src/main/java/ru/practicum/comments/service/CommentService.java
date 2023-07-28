package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentInDto;
import ru.practicum.comments.dto.CommentOutDto;
import ru.practicum.comments.model.CommentsSearchParams;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    CommentOutDto addComment(CommentInDto inDto, long eventId, Optional<Long> parentId, long userId);

    CommentOutDto updateComment(String text, long userId, long commentId, String status);

    CommentOutDto deleteComment(long userId, long commentId);

    CommentOutDto getCommentByIdForPrivate(long userId, long commentId);

    List<CommentOutDto> getAllComment(CommentsSearchParams params);

    CommentOutDto getCommentByIdForAdmin(long commentId);

    List<CommentOutDto> updateCommentByIdForAdmin(List<Long> commentIds, String status);
}
