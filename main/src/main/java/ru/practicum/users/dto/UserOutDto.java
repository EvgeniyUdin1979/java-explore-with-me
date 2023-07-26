package ru.practicum.users.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserOutDto {
    private long id;
    private String email;
    private String name;
}
