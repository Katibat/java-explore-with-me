package ru.practicum.explorewithme.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.compilation.model.*;
import ru.practicum.explorewithme.event.EventMapper;
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
    private final EventRepository eventRepository;
    private final CompilationMapper mapper;
    private final EventMapper eventMapper;

    @Transactional
    public CompilationDto save(CompilationCreateDto compilationCreateDto) {
        Compilation compilation = mapper.toModel(compilationCreateDto);
        Compilation saved = repository.save(compilation);
        log.info("CompilationAdminService: Сохранена подборка событий с id={}.", saved.getId());
        return mapper.toDto(saved, findCompilationEvents(saved));
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
        List<Event> events = findCompilationEvents(compilation)
                .stream().map(eventMapper::toModelFromShortDto).collect(Collectors.toList());
        if (isDeleting) {
            if (events.contains(event)) {
                compilation.getEvents().remove(event);
                log.info("CompilationAdminService: Удалено событие с id={} из подборки событий с id={}.",
                        eventId, compId);
            }
        } else {
            if (!events.contains(event)) {
                compilation.getEvents().add(event);
                log.info("CompilationAdminService: Добавлено событие с id={} в подборку событий с id={}.",
                        eventId, compId);
            }
        }
        repository.save(compilation);
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

    private List<EventShortDto> findCompilationEvents(Compilation compilation) {
        return compilation.getEvents()
                .stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    private Compilation findCompilationById(Long compilationId) {
        return repository.findById(compilationId)
                .orElseThrow(() ->
                        new NotFoundException("CompilationService: Не найдена подборка событий с id=" + compilationId));
    }
}