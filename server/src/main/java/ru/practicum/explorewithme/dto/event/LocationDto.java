package ru.practicum.explorewithme.dto.event;

import lombok.*;

import javax.persistence.*;

/**
 * Информация о месте проведения события (широта / долгота)
 */

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private Float lat;
    private Float lon;
}