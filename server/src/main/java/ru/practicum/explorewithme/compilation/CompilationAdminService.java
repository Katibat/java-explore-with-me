package ru.practicum.explorewithme.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.compilation.model.*;
import ru.practicum.explorewithme.event.EventMapper;
import ru.practicum.explorewithme.event.EventPrivateService;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventShortDto;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.exception.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationAdminService {
    private final CompilationRepository repository;
    private final CompilationEventsRepository compilationEventsRepository;
    private final EventRepository eventRepository;
    private final EventPrivateService eventPrivateService;
    private final CompilationMapper mapper;
    private final EventMapper eventMapper;

    @Transactional
    public CompilationDto save(CompilationCreateDto compilationCreateDto) {
        Compilation compilation = mapper.toModel(compilationCreateDto);
        Compilation savedComp = repository.save(compilation);
        for (Long eventId : compilationCreateDto.getEvents()) {
            CompilationEvents compilationEvents = CompilationEvents.builder()
                    .compilation(savedComp.getId())
                    .event(eventId)
                    .build();
            compilationEventsRepository.save(compilationEvents);
        }
        log.info("CompilationAdminService: Сохранена подборка событий с id={}.", savedComp.getId());
        return mapper.toDto(savedComp, findCompilationEvents(savedComp.getId()));
    }

    @Transactional
    public void delete(Long compId) {
        Compilation compilation = repository.findById(compId)
                .orElseThrow(() ->
                        new NotFoundException("CompilationAdminService: Не найдена подборка событий с id=" +
                                compId));
        repository.delete(compilation);
        log.info("CompilationAdminService: Удалена информация о подборке событий №={}.", compId);
    }

    @Transactional
    public void saveOrDeleteEventInCompilation(Long compId, Long eventId, boolean isDeleting) {
        Compilation compilation = findCompilationById(compId);
        Event event = findEventById(eventId);
        List<Event> events = findCompilationEvents(compilation.getId())
                .stream().map(eventMapper::toModelFromShortDto).collect(Collectors.toList());
        if (isDeleting) {
            if (events.contains(event)) {
                compilationEventsRepository.deleteByCompilationAndEvent(compId, eventId);
                log.info("CompilationAdminService: Удалено событие с id={} из подборки событий с id={}.",
                        eventId, compId);
            }
        } else {
            if (!events.contains(event)) {
                CompilationEvents compilationEvents = CompilationEvents.builder()
                        .compilation(compId)
                        .event(eventId)
                        .build();
                compilationEventsRepository.save(compilationEvents);
                log.info("CompilationAdminService: Добавлено событие с id={} в подборку событий с id={}.",
                        eventId, compId);
            }
        }
    }

    @Transactional
    public void changeCompilationPin(Long compilationId, boolean isDeleting) {
        Compilation compilation = findCompilationById(compilationId);
        boolean pinned = compilation.isPinned();
        if (isDeleting) {
            if (pinned) {
                repository.setCompilationPinned(false, compilationId);
            } else {
                throw new ForbiddenException(String.format("CompilationAdminService: Уже откреплена подборка с id=" +
                        compilationId));
            }
        } else {
            if (!pinned) {
                repository.setCompilationPinned(true, compilationId);
            } else {
                throw new ForbiddenException(String.format("CompilationAdminService: Уже закреплена подборка с id=" +
                        compilationId));
            }
        }
    }

    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException("CompilationService: Не найдено событие с id=" + eventId));
    }

    private List<EventShortDto> findCompilationEvents(Long compId) {
        List<Long> ids = compilationEventsRepository.findCompilationEventIds(compId);
        return eventPrivateService.findEventsByIds(ids);
    }

    private Compilation findCompilationById(Long compilationId) {
        return repository.findById(compilationId)
                .orElseThrow(() ->
                        new NotFoundException("CompilationService: Не найдена подборка событий с id=" + compilationId));
    }
}