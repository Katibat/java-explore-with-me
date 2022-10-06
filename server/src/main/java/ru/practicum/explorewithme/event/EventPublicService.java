package ru.practicum.explorewithme.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.client.StatsClient;
import ru.practicum.explorewithme.event.model.*;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exception.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPublicService {
    private final StatsClient client;
    private final EventRepository repository;

    public List<EventShortDto> getEventsSort(String text,
                                             Long[] categories,
                                             Boolean paid,
                                             LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd,
                                             boolean onlyAvailable,
                                             EventSort sort,
                                             int from,
                                             int size) {
        log.info("EventPublicService: Получение списка событий с сортировкой по text={}, start={}, end={}.",
                text, rangeStart, rangeEnd);
        List<Event> events = repository.findEventsByPublicFilters(text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable);
        return getSortedEvents(events, from, size, sort);
    }

    public EventFullDto findEventById(Long eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException("EventPublicService: Не найдено событие с id=" + eventId));
        String uri = "/events/" + event.getId();
        Integer views = (Integer) client.getViews(uri);
        log.info("EventPublicService: Получение подробной информации об опубликованном событии с id={}.", eventId);
        return EventMapper.toFullDto(event, views);
    }

    private List<EventShortDto> getSortedEvents(List<Event> events, int from, int size, EventSort sort) {
        log.info("EventPublicService: Сортировка списка событий, где from={}, int={}, sort={}.", from, size, sort);
        List<Event> eventsList = getPage(from, size, events).getContent();
        return eventsList.stream()
                .map(event -> {
                    String uri = "/events/" + event.getId();
                    int views = (int) client.getViews(uri);
                    return EventMapper.toShortDto(event, views);
                })
                .sorted((o1, o2) -> {
                    if (sort != null) {
                        switch (sort) {
                            case EVENT_DATE:
                                return o1.getEventDate().compareTo(o2.getEventDate());
                            case VIEWS:
                                return o1.getViews().compareTo(o2.getViews());
                            default:
                                return o1.getId().compareTo(o2.getId());
                        }
                    } else {
                        return o1.getId().compareTo(o2.getId());
                    }
                })
                .collect(Collectors.toList());
    }

    private Page<Event> getPage(int from, int size, List<Event> events) {
        Pageable pageable = getPageable(from, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), events.size());
        return new PageImpl<>(events.subList(start, end), pageable, events.size());
    }

    private Pageable getPageable(int from, int size) {
        return PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "id"));
    }
}