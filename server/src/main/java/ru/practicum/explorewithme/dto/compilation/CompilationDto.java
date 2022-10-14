package ru.practicum.explorewithme.dto.compilation;

import lombok.*;
import ru.practicum.explorewithme.dto.event.EventShortDto;

import java.util.List;

/**
 * Информация о подборке событий
 */

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