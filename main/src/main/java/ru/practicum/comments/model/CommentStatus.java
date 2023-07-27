package ru.practicum.comments.model;

import java.util.Optional;

public enum CommentStatus {
    PENDING,
    PUBLISHED,
    REJECT,
    DELETED;

    public static Optional<CommentStatus> from(String stringState) {
        for (CommentStatus commentStatus : values()) {
            if (commentStatus.name().equalsIgnoreCase(stringState)) {
                return java.util.Optional.of(commentStatus);
            }
        }
        return java.util.Optional.empty();
    }
}
