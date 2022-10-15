package ru.practicum.explorewithme.service.pub.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.mapper.CompilationMapper;
import ru.practicum.explorewithme.repository.CompilationRepository;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.mapper.EventMapper;
import ru.practicum.explorewithme.model.compilation.Compilation;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.service.pub.CompilationPublicService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationPublicServiceImpl implements CompilationPublicService {
    private final CompilationRepository repository;
    private final CompilationMapper mapper;
    private final EventMapper eventMapper;

    @Override
    public List<CompilationDto> findAll(Boolean pinned, int from, int size) {
        return repository.findAllByPinned(pinned, PageRequest.of(from / size, size))
                .stream()
                .map(c -> mapper.toDto(c, findCompilationEvents(c)))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto findById(Long compId) {
        Compilation compilation = repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("CompilationPublicService: Не найдена подборка событий " +
                        "с id=" + compId));
        return mapper.toDto(compilation, findCompilationEvents(compilation));
    }

    /**
     * Получить список событий из подборки событий
     * @param compilation объект подборки событий
     * @return List<EventShortDto>
     */
    private List<EventShortDto> findCompilationEvents(Compilation compilation) {
        List<Event> eventList = new ArrayList<>(compilation.getEvents());
        return eventList.stream().map(eventMapper::toShortDto).collect(Collectors.toList());
    }
}