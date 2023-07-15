package ru.practicum.request.model;

import java.util.Optional;

public enum Status {
    CONFIRMED,
    PENDING;
    public static Optional<Status> from(String stringState) {
        for (Status state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
