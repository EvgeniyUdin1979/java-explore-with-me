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

    private long event_id;

    private Long parent_id;

    private CommentStatus status;

    private LocalDateTime created;

}
