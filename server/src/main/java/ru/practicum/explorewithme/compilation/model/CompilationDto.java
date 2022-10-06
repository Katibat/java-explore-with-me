package ru.practicum.explorewithme.compilation.model;

import lombok.*;
import ru.practicum.explorewithme.event.model.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private Long id;
    private String title;
    private Boolean pinned;
    private List<EventShortDto> events;
}