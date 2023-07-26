package ru.practicum.events.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import ru.practicum.events.model.EventAdminSearchParams;
import ru.practicum.events.model.State;

import java.util.List;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
class EventStorageImplTest {

    private final EventStorageDao storage;

    @Autowired
    EventStorageImplTest(EventStorageDao storage) {
        this.storage = storage;
    }

    @Test
    void findAllForAdmin() {
        EventAdminSearchParams params = EventAdminSearchParams.builder()
                .users(List.of(1L, 2L))
                .categories(List.of(1L, 2L))
                .states(List.of(State.PENDING))
                .from(0)
                .size(1)
                .build();
        storage.findAllForAdmin(params).forEach(System.out::println);
    }
}