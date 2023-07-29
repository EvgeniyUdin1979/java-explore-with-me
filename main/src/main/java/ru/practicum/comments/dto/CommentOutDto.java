package ru.practicum.comments.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.comments.model.CommentStatus;
import ru.practicum.users.dto.UserOutShortDto;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentOutDto {
    private long id;
    private String text;
    private UserOutShortDto creator;
    private long eventId;
    private CommentOutShortDto parent;
    private CommentStatus status;
    private LocalDateTime created;
    private LocalDateTime publishedOn;

}
