package ru.practicum.explorewithme.service.admin.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.mapper.CompilationMapper;
import ru.practicum.explorewithme.repository.CompilationRepository;
import ru.practicum.explorewithme.dto.compilation.CompilationCreateDto;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.mapper.EventMapper;
import ru.practicum.explorewithme.model.compilation.Compilation;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.exception.*;
import ru.practicum.explorewithme.service.admin.CompilationAdminService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationAdminServiceImpl implements CompilationAdminService {
    private final CompilationRepository repository;
    private final EventRepository eventRepository;
    private final CompilationMapper mapper;
    private final EventMapper eventMapper;

    @Transactional
    @Override
    public CompilationDto save(CompilationCreateDto compilationCreateDto) {
        Compilation compilation = mapper.toModel(compilationCreateDto);
        Compilation saved = repository.save(compilation);
        log.info("CompilationAdminService: Сохранена подборка событий с id={}.", saved.getId());
        return mapper.toDto(saved, findCompilationEvents(saved));
    }

    @Transactional
    @Override
    public void deleteById(Long compId) {
        repository.deleteById(compId);
        log.info("CompilationAdminService: Удалена информация о подборке событий №={}.", compId);
    }

    @Transactional
    @Override
    public void saveOrDeleteEventInCompilation(Long compId, Long eventId, boolean isDeleting) {
        Compilation compilation = findCompilationById(compId);
        Event event = findEventById(eventId);
        Set<Event> events = compilation.getEvents();
        if (isDeleting) {
            if (events.contains(event)) {
                events.remove(event);
                log.info("CompilationAdminService: Удалено событие с id={} из подборки событий с id={}.",
                        eventId, compId);
            }
        } else {
            if (!events.contains(event)) {
                events.add(event);
                log.info("CompilationAdminService: Добавлено событие с id={} в подборку событий с id={}.",
                        eventId, compId);
            }
        }
        repository.save(compilation);
    }

    @Transactional
    @Override
    public void changeCompilationPin(Long compId, boolean isPin) {
        Compilation compilation = findCompilationById(compId);
        boolean pinned = compilation.isPinned();
        if (isPin) {
            if (pinned) {
                repository.setCompilationPinned(false, compId);
            } else {
                throw new ForbiddenException(String.format("CompilationAdminService: Уже откреплена подборка с id=" +
                        compId));
            }
        } else {
            if (!pinned) {
                repository.setCompilationPinned(true, compId);
            } else {
                throw new ForbiddenException(String.format("CompilationAdminService: Уже закреплена подборка с id=" +
                        compId));
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