package ru.practicum.events.model;

import java.util.Optional;

public enum State {
    PENDING,
    PUBLISHED,
    CANCELED;
    public static Optional<State> from(String stringState) {
        for (State state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return java.util.Optional.of(state);
            }
        }
        return java.util.Optional.empty();
    }
}
