package ru.practicum.explorewithme.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventUpdateDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private Long eventId;
    private Boolean paid;
    private Integer participantLimit;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}