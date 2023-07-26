package ru.practicum.request.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.Optional;

@Repository
public class RequestStorageImpl implements RequestStorageDao {

    private final RequestRepository requestRepository;

    @Autowired
    public RequestStorageImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public List<Request> findAllRequestsById(List<Long> requestsId) {
        return requestRepository.findAllByIdIn(requestsId);
    }

    public void updateAllRequests(List<Request> requests) {
        requestRepository.saveAll(requests);
    }

    @Override
    public List<Request> findAllByRequesterId(long userId) {
        return requestRepository.findAllByRequesterId(userId);
    }

    @Override
    public Request add(Request request) {
        return requestRepository.save(request);
    }

    @Override
    public Optional<Request> findById(long requestsId) {
        return requestRepository.findById(requestsId);
    }
}
