package ru.practicum.explorewithme.compilation;

import lombok.*;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.compilation.model.*;
import ru.practicum.explorewithme.event.model.*;

import java.util.List;

@Component
public class CompilationMapper {

    public Compilation toModel(CompilationCreateDto compilationCreateDto) {
        return Compilation.builder()
                .title(compilationCreateDto.getTitle())
                .pinned(compilationCreateDto.getPinned())
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