package ru.practicum.explorewithme.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.event.model.*;
import ru.practicum.explorewithme.exception.*;
import ru.practicum.explorewithme.request.*;
import ru.practicum.explorewithme.request.model.*;
import ru.practicum.explorewithme.user.UserRepository;

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
    private final RequestRepository requestRepository;
    private final EventMapper mapper;
    private final RequestMapper requestMapper;

    public List<EventFullDto> findEventByInitiatorId(Long userId, int from, int size) {
        findUserById(userId);
        Pageable pageable = PageRequest.of(from / size, size);
        return repository.findAllByInitiatorId(userId, pageable)
                .stream()
                .map(mapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventFullDto update(EventUpdateDto eventUpdateDto, Long userId) {
        Event event = findEventById(eventUpdateDto.getEventId());
        isInitiator(event, userId);

        if (event.getState().equals(EventState.PENDING) || event.getState().equals(CANCELED)) {
            throw new ForbiddenException("EventPrivateService: Для обновлении передан неверный статус события state=" +
                    event.getState());
        }
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
    public EventFullDto save(EventCreateDto eventCreateDto, Long userId) {
        Event event = mapper.toModel(eventCreateDto, userId);
        Event saved = repository.save(event);
        log.info("EventPrivateService: Сохранено событие={}.", saved);
        return mapper.toFullDto(saved);
    }

    public EventFullDto findEventInfoByInitiator(Long userId, Long eventId) {
        Event event = findEventById(eventId);
        isInitiator(event, userId);
        return mapper.toFullDto(event);
    }

    @Transactional
    public EventFullDto cancel(Long userId, Long eventId) {
        EventFullDto eventFullDto = findEventInfoByInitiator(userId, eventId);
        if (eventFullDto.getState().equals("CANCELED")) {
            throw new ForbiddenException("EventPrivateService: Событие уже отклонено (CANCELED).");
        }
        repository.cancelEvent(CANCELED, eventId);
        eventFullDto.setState("CANCELED");
        log.info("EventPrivateService: Отменено событие с id={}.", eventFullDto.getId());
        return eventFullDto;
    }

    public List<RequestDto> getAllRequestsByEventId(Long userId, Long eventId) {
        Event event = findEventById(eventId);
        isInitiator(event, userId);
        return requestRepository.findAllByEvent(event.getId())
                .stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
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
        log.info("EventPrivateService: Изменен статус заявки на участие в событии с id={}.", changed.getId());
        if (event.getConfirmedRequests() == event.getParticipantLimit() - 1) {
            requestRepository.rejectPendingRequests(eventId);
        }
        return requestMapper.toDto(changed);
    }

    private Event findEventById(Long eventId) {
        return repository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException("EventPrivateService: Не найдено событие с id=" + eventId));
    }

    public List<EventShortDto> findEventsByIds(List<Long> ids) { // using in CompilationAdminService
        return repository.findAllById(ids)
                .stream()
                .map(mapper::toShortDto)
                .collect(Collectors.toList());
    }

    public Request findRequestById(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() ->
                        new NotFoundException("EventPrivateService: Не найден запрос на участие в событии с id=" +
                                requestId));
    }

    public void findUserById(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("EventPrivateService: Не найден пользователь с id=" + userId));
    }

    private void isInitiator(Event event, Long userId) {
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenException("EventPrivateService: Попытка не инициатором получить информацию " +
                    "о событии с id=" + event.getId());
        }
    }
}