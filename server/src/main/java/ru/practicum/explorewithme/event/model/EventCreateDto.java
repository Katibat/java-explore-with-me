package ru.practicum.explorewithme.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.explorewithme.event.location.LocationDto;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateDto {
    @NotNull
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Long category;
    @NotNull
    @NotBlank
    @Size(min = 20, max = 254)
    private String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private LocationDto location;
    @NotNull
    private Boolean paid = false;
    @PositiveOrZero
    private Integer participantLimit = 0;
    @NotNull
    private Boolean requestModeration = true;
    @NotNull
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}