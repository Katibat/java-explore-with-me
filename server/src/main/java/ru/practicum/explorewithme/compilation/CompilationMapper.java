package ru.practicum.explorewithme.compilation;

import lombok.*;
import ru.practicum.explorewithme.compilation.model.*;
import ru.practicum.explorewithme.event.model.*;

import java.util.List;

@RequiredArgsConstructor
public class CompilationMapper {

    public static Compilation toModel(CompilationCreateDto compilationCreateDto) {
        return Compilation.builder()
                .title(compilationCreateDto.getTitle())
                .pinned(compilationCreateDto.getPinned())
                .build();
    }

    public static CompilationDto toDto(Compilation compilation, List<EventShortDto> events) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .events(events)
                .build();
    }
}