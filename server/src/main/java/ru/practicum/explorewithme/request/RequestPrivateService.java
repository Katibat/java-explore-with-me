package ru.practicum.explorewithme.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.exception.*;
import ru.practicum.explorewithme.request.model.*;
import ru.practicum.explorewithme.user.UserRepository;
import ru.practicum.explorewithme.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestPrivateService {
    private final RequestRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestMapper mapper;

    public List<RequestDto> findRequests(Long userId) {
        log.info("RequestPrivateService: Получение информации о заявках текущего пользователя id={} " +
                "на участие в чужих событиях.", userId);
        User user = findUserById(userId);
        return repository.findAllByRequester(userId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public RequestDto save(Long eventId, Long userId) {
        Event event = findEventById(eventId);
        User user = findUserById(userId);
        checkUserNotEventsInitiator(user, event);
        checkEventStateIsPublished(event);
        checkRequestsLimit(event);
        checkIsRequestExist(userId, eventId);
        Request request = Request.builder()
                .event(eventId)
                .requester(userId)
                .build();
        if (!event.isRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }
        repository.save(request);
        log.info("RequestPrivateService: Сохранена заявка на участие в событии с id={} текущего пользователя с id={}.",
                eventId, userId);
        return mapper.toDto(request);
    }

    @Transactional
    public RequestDto cancelRequest(Long userId, Long requestId) {
        User user = findUserById(userId);
        Request request = findRequestById(requestId);
        if (!Objects.equals(request.getRequester(), user.getId())) {
            throw new ForbiddenException("RequestPrivateService: Отменить запрос на участие в мероприятии " +
                    "может только пользователь его создавший.");
        }
        if (!Objects.equals(request.getStatus(), RequestStatus.CANCELED)) {
            throw new ForbiddenException("RequestPrivateService: Запрос на участие в мероприятии уже отменен.");
        }
        request.setStatus(RequestStatus.CANCELED);
        repository.save(request);
        log.info("RequestPrivateService: Отменена заявка на участие в событии с id={} текущего пользователя с id={}.",
                request.getEvent(), userId);
        return mapper.toDto(request);
    }

    private Request findRequestById(Long requestId) {
        return repository.findById(requestId)
                .orElseThrow(() ->
                        new NotFoundException("RequestPrivateService: Не найден запрос с id=" + requestId));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("RequestPrivateService: Не найден пользователь с id=" + userId));
    }

    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException("RequestPrivateService: Не найдено событие с id=" + eventId));
    }

    private void checkRequestsLimit(Event event) {
        int confirmedRequests = repository.countByEventAndStatus(event.getId(), RequestStatus.CONFIRMED);
        int participantLimit = event.getParticipantLimit();
        if (participantLimit != 0) {
            if (participantLimit <= confirmedRequests) {
                throw new ForbiddenException("RequestPrivateService: Превышен лимит участников на событие с id=" +
                        event.getId());
            }
        }
    }

    private void checkEventStateIsPublished(Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException("RequestPrivateService: Направлен запрос на неопубликованное событие с id="
                    + event.getId());
        }
    }

    private void checkUserNotEventsInitiator(User requester, Event event) {
        if (Objects.equals(event.getInitiator().getId(), requester.getId())) {
            throw new ForbiddenException("RequestPrivateService: Запрос на участие направлен инициатором событис с id="
                    + event.getId());
        }
    }

    private void checkIsRequestExist(Long userId, Long eventId) {
        Optional<RequestDto> foundDuplicate = findRequests(userId)
                .stream()
                .filter(r -> r.getEvent().equals(eventId))
                .findFirst();
        if (foundDuplicate.isPresent()) {
            throw new ForbiddenException("RequestPrivateService: Пользователем уже направлен запрос " +
                    "на участие в событии с id=" + eventId);
        }
    }
}