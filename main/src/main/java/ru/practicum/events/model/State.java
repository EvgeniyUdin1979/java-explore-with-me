package ru.practicum.events.model;

import java.util.Optional;

public enum State {
    PENDING(StateAction.SEND_TO_REVIEW),
    PUBLISHED(StateAction.PUBLISH_EVENT),
    REJECT(StateAction.REJECT_EVENT),
    CANCELED(StateAction.CANCEL_REVIEW);

    public final StateAction stateAction;

    State(StateAction stateAction) {
        this.stateAction = stateAction;
    }

    public static Optional<State> valueOfStateAction(StateAction stateAction) {
        for (State e : values()) {
            if (e.stateAction.equals(stateAction)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    public static Optional<State> from(String stringState) {
        for (State state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return java.util.Optional.of(state);
            }
        }
        return java.util.Optional.empty();
    }
}
