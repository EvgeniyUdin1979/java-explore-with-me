package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentInDto;
import ru.practicum.comments.dto.CommentOutDto;
import ru.practicum.comments.dto.CommentUpdateInDto;
import ru.practicum.comments.model.CommentsPrivateSearchParams;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    CommentOutDto addComment(CommentInDto inDto, long eventId, Optional<Long> parentId, long userId);

    CommentOutDto updateComment(CommentUpdateInDto inDto, long userId, long commentId);

    CommentOutDto deleteComment(long userId, long commentId);

    CommentOutDto getCommentByIdForPrivate(long userId, long commentId);

    List<CommentOutDto> getAllCommentForPrivate(CommentsPrivateSearchParams params);

    CommentOutDto getCommentByIdForAdmin(long commentId);

    CommentOutDto updateCommentByIdForAdmin(long commentId, String status);
}
