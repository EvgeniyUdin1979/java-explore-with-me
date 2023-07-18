package ru.practicum.events.storage;

import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import ru.practicum.events.model.*;
import ru.practicum.request.model.QRequest;
import ru.practicum.request.storage.RequestRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class EventStorageImpl implements EventStorageDao {

    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    private final EntityManager em;

    @Autowired
    public EventStorageImpl(EventRepository eventRepository, RequestRepository requestRepository, EntityManager em) {
        this.eventRepository = eventRepository;
        this.requestRepository = requestRepository;
        this.em = em;
    }

    @Override
    public Event add(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event update(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Optional<Event> findById(long id) {
        return eventRepository.findById(id);
    }

    @Override
    public List<Event> findAllByUserId(long userId, int from, int size) {
        return eventRepository.findAllByInitiatorId(userId, PageRequest.of(from, size)).toList();
    }



    @Override
    public List<Event> findAllForAdmin(EventAdminSearchParams params) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QEvent event = QEvent.event;
        JPAQuery<Event> eventJPAQuery = queryFactory.selectFrom(event);

        if (params.getUsers() != null && !params.getUsers().isEmpty()) {
            eventJPAQuery.where(event.initiator.id.in(params.getUsers()));
        }
        if (params.getCategories() != null && !params.getCategories().isEmpty()) {
            eventJPAQuery.where(event.category.id.in(params.getCategories()));
        }
        if (params.getStates() != null && !params.getStates().isEmpty()) {
            eventJPAQuery.where(event.state.in(params.getStates()));
        }
        if (params.getRangeStart() != null) {
            eventJPAQuery.where(event.eventDate.after(params.getRangeStart()));
        }
        if (params.getRangeEnd() != null) {
            eventJPAQuery.where(event.eventDate.before(params.getRangeEnd()));
        }

        return eventJPAQuery
                .offset(params.getFrom())
                .limit(params.getSize())
                .fetch();
    }

    public List<Event> findAllForPublic(EventPublicSearchParams params) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        DateExpression<LocalDateTime> date = Expressions.asDate(LocalDateTime.now());
        QEvent event = QEvent.event;
        QRequest request =QRequest.request;
        JPAQuery<Event> eventJPAQuery = queryFactory.selectFrom(event).innerJoin(event.requests, request);
        JPAQuery<Event> query = queryFactory.selectFrom(event);

        eventJPAQuery.where(event.state.eq(State.PUBLISHED));
        if (params.getText() != null){
            eventJPAQuery.where(event.annotation.containsIgnoreCase(params.getText()));
            eventJPAQuery.where(event.description.containsIgnoreCase(params.getText()));
        }
        if (params.getCategories() != null && params.getCategories().isEmpty()){
            eventJPAQuery.where(event.category.id.in(params.getCategories()));
        }
        if (params.getPaid() != null) {
            eventJPAQuery.where(event.paid.gt(params.getPaid()));
        }
        if (params.getRangeStart() != null) {
            eventJPAQuery.where(event.eventDate.after(params.getRangeStart()));
        } else {
            eventJPAQuery.where(event.eventDate.after(date));
        }
        if (params.getRangeEnd() != null) {
            eventJPAQuery.where(event.eventDate.before(params.getRangeEnd()));
        }
        return eventJPAQuery
                .offset(params.getFrom())
                .limit(params.getSize())
                .fetch();
    }
}
