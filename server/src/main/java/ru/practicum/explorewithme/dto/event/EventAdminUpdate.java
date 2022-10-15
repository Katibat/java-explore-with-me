package ru.practicum.explorewithme.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.explorewithme.model.event.Location;

import java.time.LocalDateTime;

/**
 * Информация для редактирования события администратором
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventAdminUpdate {
    private String annotation;
    private Long category;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}