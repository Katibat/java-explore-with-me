package ru.practicum.explorewithme.event.location;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event_locations")
public class Location {
    @Column(name = "lat")
    private Double lat;
    @Column(name = "lon")
    private Double lon;
}