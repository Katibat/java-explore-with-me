package ru.practicum.explorewithme.model.event;

import lombok.*;

import javax.persistence.*;

/**
 * Место проведения события (широта / долгота)
 */

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private Float lat;
    private Float lon;
}