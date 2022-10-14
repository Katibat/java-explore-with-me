package ru.practicum.explorewithme.service.priv.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.model.category.Category;
import ru.practicum.explorewithme.dto.request.RequestDto;
import ru.practicum.explorewithme.dto.request.RequestStatus;
import ru.practicum.explorewithme.dto.event.EventCreateDto;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventUpdateDto;
import ru.practicum.explorewithme.mapper.EventMapper;
import ru.practicum.explorewithme.mapper.RequestMapper;
import ru.practicum.explorewithme.model.request.Request;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.exception.*;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.event.EventState;
import ru.practicum.explorewithme.repository.RequestRepository;
import ru.practicum.explorewithme.repository.UserRepository;
import ru.practicum.explorewithme.service.priv.EventPrivateService;

import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.model.event.EventState.CANCELED;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPrivateServiceImpl implements EventPrivateService {
    private final EventRepository repository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final EventMapper mapper;
    private final RequestMapper requestMapper;

    @Override
    public List<EventFullDto> findEventByInitiatorId(Long userId, int from, int size) {
        findUserById(userId);
        Pageable pageable = PageRequest.of(from / size, size);
        return repository.findAllByInitiatorId(userId, pageable)
                .stream()
                .map(mapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto update(EventUpdateDto eventUpdateDto, Long userId) {
        Event event = findEventById(eventUpdateDto.getEventId());
        isInitiator(event, userId);
        if (eventUpdateDto.getTitle() != null) event.setTitle(eventUpdateDto.getTitle());
        if (eventUpdateDto.getAnnotation() != null) event.setAnnotation(eventUpdateDto.getAnnotation());
        if (eventUpdateDto.getDescription() != null) event.setDescription(eventUpdateDto.getDescription());
        if (eventUpdateDto.getEventDate() != null) event.setEventDate(eventUpdateDto.getEventDate());
        if (eventUpdateDto.getPaid() != null) event.setPaid(eventUpdateDto.getPaid());
        if (eventUpdateDto.getCategory() != null) {
            event.setCategory(new Category(eventUpdateDto.getCategory(), null));
        }
        if (eventUpdateDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdateDto.getParticipantLimit());
        }
        event.setState(EventState.PENDING);
        Event updated = repository.save(event);
        log.info("EventPrivateService: Обновлено событие с id={}.", updated.getId());
        return mapper.toFullDto(updated);
    }

    @Transactional
    @Override
    public EventFullDto save(EventCreateDto eventCreateDto, Long userId) {
        Event event = mapper.toModel(eventCreateDto, userId);
        Event saved = repository.save(event);
        log.info("EventPrivateService: Сохранено событие={}.", saved);
        return mapper.toFullDto(saved);
    }

    @Override
    public EventFullDto findEventInfoByInitiator(Long userId, Long eventId) {
        Event event = findEventById(eventId);
        isInitiator(event, userId);
        return mapper.toFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto cancel(Long userId, Long eventId) {
        EventFullDto eventFullDto = findEventInfoByInitiator(userId, eventId);
        repository.cancelEvent(CANCELED, eventId);
        eventFullDto.setState("CANCELED");
        log.info("EventPrivateService: Отменено событие с id={}.", eventFullDto.getId());
        return eventFullDto;
    }

    @Override
    public List<RequestDto> getAllRequestsByEventId(Long userId, Long eventId) {
        findEventInfoByInitiator(userId, eventId);
        return requestRepository.findAllByEventId(eventId)
                .stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public RequestDto changeRequestStatus(Long userId, Long eventId, Long requestId, boolean isApproved) {
        EventFullDto event = findEventInfoByInitiator(userId, eventId);
        if (event.getConfirmedRequests() >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new ForbiddenException("EventPrivateService: Превышен лимит заявок на участие в событии с id=" +
                    eventId);
        }
        Request request = findRequestById(requestId);
        if (isApproved) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.REJECTED);
        }
        Request changed = requestRepository.save(request);
        log.info("EventPrivateService: Изменен статус заявки №{}, статус={} на участие в событии с id={}.",
                requestId, changed.getStatus(), changed.getId());
        if (event.getConfirmedRequests() == event.getParticipantLimit() - 1) {
            requestRepository.rejectPendingRequests(eventId);
        }
        return requestMapper.toDto(changed);
    }

    @Override
    public Event findEventById(Long eventId) {
        return repository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException("EventPrivateService: Не найдено событие с id=" + eventId));
    }

    /**
     * Искать в репозитории запроса на участие в событии по идентификатору
     * если не найден, то вернуть NotFoundException
     * @param requestId идентификатор запроса
     * @return Request
     */
    private Request findRequestById(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() ->
                        new NotFoundException("EventPrivateService: Не найден запрос на участие в событии с id=" +
                                requestId));
    }

    /**
     * Искать в репозитории пользователя по идентификатору
     * если не найден, то вернуть NotFoundException
     * @param userId идентификатор пользователя
     */
    private void findUserById(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("EventPrivateService: Не найден пользователь с id=" + userId));
    }

    /**
     * Проверить является ли пользователь инициатором события
     * если не найден, то вернуть ForbiddenException
     * @param event объект события
     * @param userId идентификатор проверяемого пользователя
     */
    private void isInitiator(Event event, Long userId) {
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenException("EventPrivateService: Попытка не инициатором внести изменения или получить " +
                    "информацию о событии с id=" + event.getId());
        }
    }
}