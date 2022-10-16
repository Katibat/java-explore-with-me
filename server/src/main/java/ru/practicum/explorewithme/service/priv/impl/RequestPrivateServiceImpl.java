package ru.practicum.explorewithme.service.priv.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.request.RequestDto;
import ru.practicum.explorewithme.dto.request.RequestStatus;
import ru.practicum.explorewithme.mapper.RequestMapper;
import ru.practicum.explorewithme.model.event.EventState;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.request.Request;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.exception.*;
import ru.practicum.explorewithme.repository.RequestRepository;
import ru.practicum.explorewithme.repository.UserRepository;
import ru.practicum.explorewithme.model.user.User;
import ru.practicum.explorewithme.service.priv.RequestPrivateService;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestPrivateServiceImpl implements RequestPrivateService {
    private final RequestRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final RequestMapper mapper;

    public List<RequestDto> findRequests(Long userId) {
        log.info("RequestPrivateService: Получение информации о заявках текущего пользователя id={} " +
                "на участие в чужих событиях.", userId);
        return repository.findAllByRequesterId(userId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public RequestDto save(Long userId, Long eventId) {
        Event event = findEventById(eventId);
        userRepository.existsById(userId);
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenException("RequestPrivateService: Запрос на участие направлен инициатором событис с id="
                    + event.getId());
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException("RequestPrivateService: Направлен запрос на участие " +
                    "в неопубликованном событии с id=" + event.getId());
        }
        int confirmedRequests = repository.getConfirmedRequests(eventId);
        int participantLimit = event.getParticipantLimit();
        if (participantLimit != 0) {
            if (participantLimit <= confirmedRequests) {
                throw new ForbiddenException("RequestPrivateService: Превышен лимит участников на событие с id=" +
                        event.getId());
            }
        }
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .eventId(eventId)
                .requesterId(userId)
                .build();
        if (event.getRequestModeration()) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
        }
        Request saved = repository.save(request);
        entityManager.refresh(saved);
        log.info("RequestPrivateService: Сохранена заявка на участие c id={} в событии с id={} " +
                "текущего пользователя с id={}.", saved.getId(), eventId, userId);
        return mapper.toDto(saved);
    }

    @Transactional
    public RequestDto cancelRequest(Long userId, Long requestId) {
        User user = findUserById(userId);
        Request request = findRequestById(requestId);
        if (!Objects.equals(request.getRequesterId(), user.getId())) {
            throw new ForbiddenException("RequestPrivateService: Отменить запрос на участие в мероприятии " +
                    "может только пользователь его создавший.");
        }
        if (Objects.equals(request.getStatus(), RequestStatus.CANCELED)) {
            throw new ForbiddenException("RequestPrivateService: Запрос на участие в мероприятии уже отменен.");
        }
        request.setStatus(RequestStatus.CANCELED);
        repository.save(request);
        log.info("RequestPrivateService: Отменена заявка на участие в событии с id={} текущего пользователя с id={}.",
                request.getEventId(), userId);
        return mapper.toDto(request);
    }

    /**
     * Искать в репозитории запроса на участие в событии по идентификатору
     * если не найден, то вернуть NotFoundException
     * @param requestId идентификатор запроса
     * @return Request
     */
    private Request findRequestById(Long requestId) {
        return repository.findById(requestId)
                .orElseThrow(() ->
                        new NotFoundException("RequestPrivateService: Не найден запрос с id=" + requestId));
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("RequestPrivateService: Не найден пользователь с id=" + userId));
    }

    @Override
    public Event findEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException("RequestPrivateService: Не найдено событие с id=" + eventId));
    }
}