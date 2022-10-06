package ru.practicum.explorewithme.compilation;

import lombok.*;
import ru.practicum.explorewithme.compilation.model.*;
import ru.practicum.explorewithme.event.EventMapper;
import ru.practicum.explorewithme.event.model.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CompilationMapper {

    public static Compilation toModel(CompilationCreateDto compilationCreateDto) {
        return Compilation.builder()
                .title(compilationCreateDto.getTitle())
                .pinned(compilationCreateDto.getPinned())
                .build();
    }

    public static CompilationDto toDto(Compilation compilation) {
        List<EventShortDto> events = compilation.getEvents().stream().map((Event event) ->
                EventMapper.toShortDto(event, 0)).collect(Collectors.toList());
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .events(events)
                .build();
    }
}