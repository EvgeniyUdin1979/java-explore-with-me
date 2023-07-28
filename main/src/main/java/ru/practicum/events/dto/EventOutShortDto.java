package ru.practicum.events.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.categories.dto.CategoryOutDto;
import ru.practicum.comments.dto.CommentOutDto;
import ru.practicum.users.dto.UserOutShortDto;

import java.util.List;

@Data
@Builder
public class EventOutShortDto {

    private long id;

    private String annotation;

    private CategoryOutDto category;

    private long confirmedRequests;

    private String eventDate;

    private UserOutShortDto initiator;

    private boolean paid;

    private String title;

    private long views;

    private List<CommentOutDto> comments;
}
