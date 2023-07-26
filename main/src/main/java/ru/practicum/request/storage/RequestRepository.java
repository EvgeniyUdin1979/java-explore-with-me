package ru.practicum.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request,Long> {

    List<Request> findAllByIdIn(List<Long> requestsId);

    List<Request> findAllByRequesterId(long userId);
}
