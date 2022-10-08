package ru.practicum.explorewithme.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.event.location.Location;
import ru.practicum.explorewithme.exception.*;
import ru.practicum.explorewithme.event.model.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventAdminService {
    private final EventRepository repository;

    public List<EventFullDto> searchEvents(List<Long> users,
                                           List<String> states,
                                           List<Long> categories,
                                           LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd,
                                           int from,
                                           int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<EventState> eventStates = null;
        if (states != null && !states.isEmpty()) {
            eventStates = new ArrayList<>();
            for (String state : states) {
                try {
                    eventStates.add(EventState.valueOf(state));
                } catch (IllegalArgumentException e) {
                    throw new IncorrectEventStateException("EventAdminService: При поиске списка событий передан " +
                            "некорректный статус события.");
                }
            }
        }
        if (rangeStart == null) rangeStart = LocalDateTime.now();
        return repository.searchEvents(users, eventStates, categories,
                        rangeStart, rangeEnd, pageable)
                .stream()
                .map(EventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventFullDto update(EventAdminUpdate eventAdminUpdate, Long eventId) {
        Event event = findEventById(eventId);
        if (eventAdminUpdate.getTitle() != null) event.setTitle(eventAdminUpdate.getTitle());
        if (eventAdminUpdate.getAnnotation() != null) event.setAnnotation(eventAdminUpdate.getAnnotation());
        if (eventAdminUpdate.getDescription() != null) event.setDescription(eventAdminUpdate.getDescription());
        if (eventAdminUpdate.getEventDate() != null) event.setEventDate(eventAdminUpdate.getEventDate());
        if (eventAdminUpdate.getLocation() != null) {
            event.setLocation(new Location(eventAdminUpdate.getLocation().getLat(),
                    eventAdminUpdate.getLocation().getLon()));
        }
        if (eventAdminUpdate.getPaid() != null) event.setPaid(eventAdminUpdate.getPaid());
        if (eventAdminUpdate.getCategory() != null) {
            event.setCategory(new Category(eventAdminUpdate.getCategory(), null));
        }
        if (eventAdminUpdate.getParticipantLimit() != null) {
            event.setParticipantLimit(eventAdminUpdate.getParticipantLimit());
        }
        if (eventAdminUpdate.getRequestModeration() != null) {
            event.setRequestModeration(eventAdminUpdate.getRequestModeration());
        }
        Event updated = repository.save(event);
        log.info("EventAdminService: Обновлено событие с id={}.", updated.getId());
        return EventMapper.toFullDto(updated);
    }

    @Transactional
    public EventFullDto changeEventState(Long eventId, boolean isPublish) {
        Event event = findEventById(eventId);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ForbiddenException("EventPrivateService: Событие не может быть опубликовано ранее, " +
                    "чем за 1 час до начала.");
        }
        if (!event.getState().equals(EventState.PENDING)) {
            throw new ForbiddenException("EventAdminService: Не установлен статус PENDING " +
                    "для события с id=" + eventId);
        }
        if (isPublish) {
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        } else {
            event.setState(EventState.CANCELED);
        }
        Event changed = repository.save(event);
        return EventMapper.toFullDto(changed);
    }

    public Event findEventById(Long eventId) { // using in CompilationAdminService
        return repository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException("EventAdminService: Не найдено событие с id=" + eventId));
    }
}