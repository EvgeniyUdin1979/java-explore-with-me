package ru.practicum.request.storage;

import ru.practicum.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestStorageDao {
    List<Request> findAllRequestsById(List<Long> requestsId);

    void updateAllRequests(List<Request> requests);

    List<Request> findAllByRequesterId(long userId);

    Request add(Request request);

    Optional<Request> findById(long requestsId);
}
