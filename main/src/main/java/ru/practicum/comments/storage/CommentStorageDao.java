package ru.practicum.comments.storage;

import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentsPrivateSearchParams;

import java.util.List;
import java.util.Optional;

public interface CommentStorageDao {
    Optional<Comment> findById(long commentId);

    Comment add(Comment comment);

    Comment update(Comment comment);

    List<Comment> getAllCommentForPrivate(CommentsPrivateSearchParams params);
}
