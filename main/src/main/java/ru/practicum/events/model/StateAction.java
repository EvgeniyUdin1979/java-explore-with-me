package ru.practicum.events.model;

import java.util.Optional;

public enum StateAction {
    SEND_TO_REVIEW,
    CANCEL_REVIEW,
    REJECT_EVENT,
    PUBLISH_EVENT;
    public static Optional<StateAction> from(String stringStateAction) {
        for (StateAction stateAction : values()) {
            if (stateAction.name().equalsIgnoreCase(stringStateAction)) {
                return Optional.of(stateAction);
            }
        }
        return Optional.empty();
    }
}
