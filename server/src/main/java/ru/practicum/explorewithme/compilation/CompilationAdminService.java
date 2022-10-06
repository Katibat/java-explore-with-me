package ru.practicum.explorewithme.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.compilation.model.*;
import ru.practicum.explorewithme.event.EventPrivateService;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.repository.EventRepository;
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
    private final EventPrivateService eventPrivateService;

    @Transactional
    public CompilationDto save(CompilationCreateDto compilationCreateDto) {
        List<Long> eventsId = compilationCreateDto.getEvents();
        Compilation compilation = CompilationMapper.toModel(compilationCreateDto);
        Set<Event> events = eventsId.stream()
                .map(eventPrivateService::findEventById)
                .collect(Collectors.toSet());
        compilation.setEvents(events);
        repository.save(compilation);
        return CompilationMapper.toDto(compilation);
    }

    @Transactional
    public void delete(Long compilationId) {
        Compilation compilation = repository.findById(compilationId)
                .orElseThrow(() ->
                        new NotFoundException("CompilationAdminService: Не найдена подборка событий с id=" +
                                compilationId));
        repository.delete(compilation);
        log.info("CompilationAdminService: Удалена информация о подборке событий №={}.", compilationId);
    }

    @Transactional
    public void saveOrDeleteEventInCompilation(Long compilationId, Long eventId, boolean isDeleting) {
        Compilation compilation = findCompilationById(compilationId);
        Event event = findEventById(eventId);
        if (isDeleting) {
            if (compilation.getEvents().contains(event)) {
                repository.deleteEventFromCompilation(compilationId, eventId);
            }
        } else {
            if (!compilation.getEvents().contains(event)) {
                repository.addEventToCompilation(compilationId, eventId);
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

    private Compilation findCompilationById(Long compilationId) {
        return repository.findById(compilationId)
                .orElseThrow(() ->
                        new NotFoundException("CompilationService: Не найдена подборка событий с id=" + compilationId));
    }
}