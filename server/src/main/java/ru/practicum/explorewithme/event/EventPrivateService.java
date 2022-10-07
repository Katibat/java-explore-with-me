package ru.practicum.explorewithme.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.category.CategoryRepository;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.client.StatsClient;
import ru.practicum.explorewithme.event.model.*;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exception.*;
import ru.practicum.explorewithme.request.*;
import ru.practicum.explorewithme.request.model.*;
import ru.practicum.explorewithme.user.UserRepository;
import ru.practicum.explorewithme.user.model.User;

import javax.persistence.EntityManager;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.event.EventState.CANCELED;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPrivateService {
    private final EventRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final EntityManager entityManager;
    private final StatsClient client;

    public List<EventShortDto> findEventByInitiatorId(Long userId, int from, int size) {
        findUserById(userId);
        Pageable pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "id"));
        List<Event> events = repository.findAllByInitiatorId(userId, pageable).getContent();
        return events.stream()
                .map(e -> {
                    Integer views = getViews(e.getId());
                    return EventMapper.toShortDto(e, views);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public EventFullDto update(EventUpdateDto eventUpdateDto, Long userId) {
        Event event = findEventById(eventUpdateDto.getEventId());
        isInitiator(event, userId);
        EventMapper.toUpdateDto(eventUpdateDto, event);
        if (event.getState().equals(EventState.PENDING) || event.getState().equals(CANCELED)) {
            throw new ForbiddenException("EventPrivateService: Для обновлении передан неверный статус события state=" +
                    event.getState());
        }
        findCategoryById(event.getCategory().getId());
        repository.save(event);
        entityManager.refresh(event);
        return EventMapper.toFullDto(event, getViews(eventUpdateDto.getEventId()));
    }

    @Transactional
    public EventFullDto save(EventCreateDto eventCreateDto, Long userId) {
        findUserById(userId);
        findCategoryById(eventCreateDto.getCategory());
        if (eventCreateDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("EventPrivateService: Событие не может быть опубликовано ранее, " +
                    "чем за 2 часа до начала.");
        }
        Event event = EventMapper.toModel(eventCreateDto, userId);
        repository.save(event);
        entityManager.refresh(event);
        return EventMapper.toFullDto(event, 0);
    }

    public EventFullDto findEventInfoByInitiator(Long userId, Long eventId) {
        Event event = findEventById(eventId);
        isInitiator(event, userId);
        return EventMapper.toFullDto(event, getViews(eventId));
    }

    @Transactional
    public EventFullDto cancel(Long userId, Long eventId) {
        EventFullDto eventFullDto = findEventInfoByInitiator(userId, eventId);
        if (eventFullDto.getState().equals(CANCELED)) {
            throw new ForbiddenException("EventPrivateService: Событие уже отклонено (CANCELED).");
        }
        repository.cancelEvent(CANCELED, eventId);
        eventFullDto.setState(CANCELED);
        return eventFullDto;
    }

    public List<RequestFullDto> getAllRequestsByEventId(Long userId, Long eventId) {
        Event event = findEventById(eventId);
        isInitiator(event, userId);
        return requestRepository.findAllByEventId(event.getId())
                .stream()
                .map(RequestMapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public RequestFullDto changeRequestStatus(Long userId, Long eventId, Long requestId, boolean isApproved) {
        Event event = findEventById(eventId);
        isInitiator(event, userId);
        findEventInfoByInitiator(userId, eventId);
        List<Request> requests = findRequestsByEvent(event);
        Request request = findRequestById(requestId);
        if (!requests.contains(request)) {
            throw new ForbiddenException("EventPrivateService:  Не найден запрос с id=" + requestId);
        }
        if (!Objects.equals(request.getStatus(), RequestStatus.PENDING)) {
            throw new ForbiddenException("EventPrivateService: Статус запроса не в ожидании(PENDING)");
        }
        if (isApproved) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.REJECTED);
        }
        requestRepository.save(request);
        RequestFullDto requestFullDto = RequestMapper.toFullDto(request);

        return RequestMapper.toFullDto(request);
    }

    public Event findEventById(Long eventId) { // using in CompilationAdminService
        return repository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException("EventPrivateService: Не найдено событие с id=" + eventId));
    }

    public Request findRequestById(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() ->
                        new NotFoundException("EventPrivateService: Не найден запрос на участие в событии с id=" +
                                requestId));
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("EventPrivateService: Не найден пользователь с id=" + userId));
    }

    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new NotFoundException("EventPrivateService: Не найдена категория с id=" + categoryId));
    }

    private void isInitiator(Event event, Long userId) {
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictException("EventPrivateService: Попытка не инициатором получить информацию " +
                    "о событии с id=" + event.getId());
        }
    }

    private List<Request> findRequestsByEvent(Event event) {
        return requestRepository.findAllByEventId(event.getId());
    }

    public Long getConfirmedRequestsCount(Event event) {
        return findRequestsByEvent(event).stream().filter(r -> r.getStatus().equals(RequestStatus.CONFIRMED)).count();
    }

    private int getViews(Long eventsId) {
        String uri = "/events/" + eventsId;
        return (Integer) client.getViews(uri);
    }
}