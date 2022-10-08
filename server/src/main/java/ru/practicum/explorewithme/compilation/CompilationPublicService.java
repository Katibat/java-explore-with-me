package ru.practicum.explorewithme.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.compilation.model.*;
import ru.practicum.explorewithme.event.EventPrivateService;
import ru.practicum.explorewithme.event.model.EventShortDto;
import ru.practicum.explorewithme.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationPublicService {
    private final CompilationRepository repository;
    private final CompilationEventsRepository compilationEventsRepository;
    private final EventPrivateService eventPrivateService;

    public List<CompilationDto> findAll(Boolean pinned, int from, int size) {
        return repository.findAllByPinned(pinned, PageRequest.of(from / size, size))
                .stream()
                .map(c -> CompilationMapper.toDto(c, findCompilationEvents(c.getId())))
                .collect(Collectors.toList());
    }

    public CompilationDto findById(Long compId) {
        Compilation compilation = repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("CompilationPublicService: Не найдена подборка событий " +
                        "с id=" + compId));
        return CompilationMapper.toDto(compilation, findCompilationEvents(compId));
    }

    private List<EventShortDto> findCompilationEvents(Long compId) {
        List<Long> ids = compilationEventsRepository.findCompilationEventIds(compId);
        return eventPrivateService.findEventsByIds(ids);
    }
}