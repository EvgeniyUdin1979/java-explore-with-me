package ru.practicum.events.storage;

import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventAdminSearchParams;
import ru.practicum.events.model.EventPublicSearchParams;

import java.util.List;
import java.util.Optional;

public interface EventStorageDao {

    Event add(Event event);

    Event update(Event event);

    Optional<Event> findById(long id);

    List<Event> findAllByUserId(long userId, int from, int size);

    List<Event> findAllForAdmin(EventAdminSearchParams params);

    List<Event> findAllForPublic(EventPublicSearchParams params);
}
