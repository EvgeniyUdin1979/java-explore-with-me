package ru.practicum.comments.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@Builder
public class CommentsSearchParams {

    private LocalDateTime rangeStart;

    private LocalDateTime rangeEnd;

    private List<Long> userIds;

    private Optional<CommentStatus> status;

    private List<Long> commentIds;

    private int from;

    private int size;
}
