package ru.practicum.explorewithme.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.compilation.model.*;
import ru.practicum.explorewithme.event.EventMapper;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventShortDto;
import ru.practicum.explorewithme.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationPublicService {
    private final CompilationRepository repository;
    private final CompilationMapper mapper;
    private final EventMapper eventMapper;

    public List<CompilationDto> findAll(Boolean pinned, int from, int size) {
        return repository.findAllByPinned(pinned, PageRequest.of(from / size, size))
                .stream()
                .map(c -> mapper.toDto(c, findCompilationEvents(c)))
                .collect(Collectors.toList());
    }

    public CompilationDto findById(Long compId) {
        Compilation compilation = repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("CompilationPublicService: Не найдена подборка событий " +
                        "с id=" + compId));
        return mapper.toDto(compilation, findCompilationEvents(compilation));
    }

    private List<EventShortDto> findCompilationEvents(Compilation compilation) {
        List<Event> eventList = new ArrayList<>(compilation.getEvents());
        return eventList.stream().map(eventMapper::toShortDto).collect(Collectors.toList());
    }
}