package ru.practicum.comments.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.users.dto.UserOutShortDto;

@Data
@Builder
public class CommentOutShortDto {

    private long id;

    private String text;

    private UserOutShortDto creator;
}
