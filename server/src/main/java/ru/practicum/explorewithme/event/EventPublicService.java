package ru.practicum.explorewithme.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.event.model.*;
import ru.practicum.explorewithme.exception.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPublicService {
    private final EventRepository repository;

    public List<EventShortDto> getEventsSort(String text,
                                             List<Long> categories,
                                             Boolean paid,
                                             LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd,
                                             boolean onlyAvailable,
                                             String sort,
                                             int from,
                                             int size) {
        log.info("EventPublicService: Получение списка событий с сортировкой по text={}, start={}, end={}.",
                text, rangeStart, rangeEnd);
        if (text.isBlank()) return Collections.emptyList();
        if (rangeStart == null) rangeStart = LocalDateTime.now();
        List<EventFullDto> events = repository.findEvents(text, categories, paid, rangeStart, rangeEnd)
                .stream()
                .map(EventMapper::toFullDto)
                .collect(Collectors.toList());
        if (onlyAvailable) {
            events = events.stream()
                    .filter(e -> e.getConfirmedRequests() < e.getParticipantLimit() || e.getParticipantLimit() == 0)
                    .collect(Collectors.toList());
        }
        if (sort != null) {
            EventSort extractedSort;
            try {
                extractedSort = EventSort.valueOf(sort);
            } catch (IllegalArgumentException e) {
                throw new IncorrectSortCriteriaException("EventPublicService: При получени списка событий передан " +
                        "некорректный вид сортировки.");
            }
            switch (extractedSort) {
                case EVENT_DATE:
                    events = events.stream()
                            .sorted(Comparator.comparing(EventFullDto::getEventDate))
                            .collect(Collectors.toList());
                    break;
                case VIEWS:
                    events = events.stream()
                            .sorted(Comparator.comparingLong(EventFullDto::getViews))
                            .collect(Collectors.toList());
                    break;
            }
        }
        return events.stream()
                .skip(from)
                .limit(size)
                .map(EventMapper::toShortDtoFromFullDto)
                .collect(Collectors.toList());
    }

    public EventFullDto findEventById(Long eventId) {
        log.info("EventPublicService: Получение подробной информации об опубликованном событии с id={}.", eventId);
        Event event = repository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException("EventPublicService: Не найдено событие с id=" + eventId));
        if (!EventState.PUBLISHED.equals(event.getState())) {
            throw new ForbiddenException("EventPublicService: Запрошена информация о " +
                    "неопубликованом событиеи с id="+ eventId);
        }
        return EventMapper.toFullDto(event);
    }
}