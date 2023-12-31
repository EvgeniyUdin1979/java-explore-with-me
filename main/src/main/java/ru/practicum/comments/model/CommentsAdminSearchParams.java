package ru.practicum.comments.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Builder
public class CommentsAdminSearchParams {

    private LocalDateTime rangeStart;

    private LocalDateTime rangeEnd;

    private Optional<Long> userId;

    private Optional<CommentStatus> status;

    private int from;

    private int size;
}
