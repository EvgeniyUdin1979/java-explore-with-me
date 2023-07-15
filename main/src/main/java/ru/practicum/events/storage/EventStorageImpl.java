package ru.practicum.events.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import ru.practicum.events.model.Event;

import java.util.List;
import java.util.Optional;

public class EventStorageImpl implements EventStorageDao {

    private final EventRepository repository;

    @Autowired
    public EventStorageImpl(EventRepository repository) {
        this.repository = repository;
    }

    @Override
    public Event add(Event event) {
        return repository.save(event);
    }

    @Override
    public Event update(Event event) {
        return repository.save(event);
    }

    @Override
    public Optional<Event> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<Event> findAllById(long id, int from, int size) {
        return repository.findAllById(id, PageRequest.of(from, size)).toList();
    }
}
