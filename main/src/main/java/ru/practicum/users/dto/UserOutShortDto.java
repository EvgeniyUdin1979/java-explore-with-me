package ru.practicum.users.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserOutShortDto {
    private long id;
    private String name;
}
