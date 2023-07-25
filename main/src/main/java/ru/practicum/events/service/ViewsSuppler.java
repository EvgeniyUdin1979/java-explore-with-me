package ru.practicum.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.client.BaseClient;
import ru.practicum.dto.StatParam;
import ru.practicum.dto.StatsOutDto;
import ru.practicum.events.dto.EventOutFullDto;
import ru.practicum.events.dto.EventOutShortDto;
import ru.practicum.events.model.Event;
import ru.practicum.events.util.EventMapping;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component

public class ViewsSuppler {
    private final BaseClient client;

    @Autowired
    public ViewsSuppler(BaseClient client) {
        this.client = client;
    }

    public List<EventOutShortDto> viewsSupplerForShortDto(List<Event> events) {
        if (!events.isEmpty()) {
            LocalDateTime start = events.stream().min(Comparator.comparing(Event::getCreated)).get().getCreated();
            List<String> urls = events.stream().map(e -> String.format("/events/%d", e.getId())).collect(Collectors.toList());
            List<StatsOutDto> allViews = client.getStats(StatParam.builder()
                    .start(start)
                    .end(LocalDateTime.now())
                    .uniqueIp(true)
                    .requestUris(urls)
                    .build());
            return events.stream().map(e -> {
                long viewHits = 0;
                for (StatsOutDto view : allViews) {
                    if (view.getUri().equals(String.format("/events/%d", e.getId()))) {
                        viewHits = view.getHits();
                        break;
                    }
                }
                return EventMapping.mapToShortDto(e, viewHits);
            }).collect(Collectors.toList());
        }
        return List.of();
    }

    public List<EventOutFullDto> viewsSupplerForFullDto(List<Event> events) {
        if (!events.isEmpty()) {
            LocalDateTime start = events.stream()
                    .min(Comparator.comparing(event -> event.getPublishedOn() == null ? event.getCreated() : event.getPublishedOn()))
                    .get()
                    .getCreated();
            List<String> urls = events.stream().map(e -> String.format("/events/%d", e.getId())).collect(Collectors.toList());
            List<StatsOutDto> allViews = client.getStats(StatParam.builder()
                    .start(start)
                    .end(LocalDateTime.now())
                    .uniqueIp(true)
                    .requestUris(urls)
                    .build());
            return events.stream().map(e -> {
                long viewHits = 0;
                for (StatsOutDto view : allViews) {
                    if (view.getUri().equals(String.format("/events/%d", e.getId()))) {
                        viewHits = view.getHits();
                        break;
                    }
                }
                return EventMapping.mapToFullDto(e, viewHits);
            }).collect(Collectors.toList());
        }
        return List.of();
    }
}
