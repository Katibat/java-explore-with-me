package ru.practicum.explorewithme.compilation;

import lombok.*;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.compilation.model.*;
import ru.practicum.explorewithme.event.EventPrivateService;
import ru.practicum.explorewithme.event.model.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventPrivateService service;

    public Compilation toModel(CompilationCreateDto compilationCreateDto) {
        Set<Event> events = compilationCreateDto.getEvents()
                .stream()
                .map(service::findEventById)
                .collect(Collectors.toSet());
        return Compilation.builder()
                .title(compilationCreateDto.getTitle())
                .pinned(compilationCreateDto.getPinned())
                .events(events)
                .build();
    }

    public CompilationDto toDto(Compilation compilation, List<EventShortDto> events) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .events(events)
                .build();
    }
}