package ru.practicum.events.storage;

import ru.practicum.events.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventStorageDao {

    Event add(Event event);

    Event update(Event event);

    Optional<Event> findById(long id);

    List<Event> findAllById(long id, int from, int size);





}
