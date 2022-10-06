package ru.practicum.explorewithme.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.client.StatsClient;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exception.*;
import ru.practicum.explorewithme.event.model.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventAdminService {
    private final EventRepository repository;
    private final StatsClient client;

    public List<EventFullDto> findEvents(Long[] initiators,
                                         EventState[] states,
                                         Long[] categories,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         int from,
                                         int size) {
        List<Event> events = repository.findEventsByAdminFilters(initiators, states, categories, rangeStart, rangeEnd);
        List<Event> eventsList = getPage(from, size, events).getContent();
        return eventsList.stream()
                .map(event -> {
                    int views = getViews(event.getId());
                    return EventMapper.toFullDto(event, views);
                })
                .collect(Collectors.toList());
    }
    @Transactional
    public EventFullDto update(EventAdminUpdate eventAdminUpdate, Long eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException("EventAdminService: Не найдено событие с id=" + eventId));
        repository.save(EventMapper.toEventAdminUpdate(eventAdminUpdate, event));
        return EventMapper.toFullDto(event, getViews(eventId));
    }

    @Transactional
    public EventFullDto changeEventState(Long eventId, boolean isPublish) {
        Event event = repository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException("EventAdminService: Не найдено событие с id=" + eventId));
        if (!event.getState().equals(EventState.PENDING)) {
            throw new ForbiddenException("EventAdminService: Статус PENDING уже установлен " +
                    "для события с id=" + eventId);
        }
        if (isPublish) {
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        } else {
            event.setState(EventState.CANCELED);
        }
        repository.save(event);
        return EventMapper.toFullDto(event, getViews(eventId));
    }

    private int getViews(Long eventsId) {
        String uri = "/events/" + eventsId;
        return (Integer) client.getViews(uri);
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