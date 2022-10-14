package ru.practicum.explorewithme.model.compilation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.practicum.explorewithme.model.event.Event;

import javax.persistence.*;
import java.util.Set;

/**
 * Подборка событий
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private boolean pinned;
    @ManyToMany
    @JoinTable(
            name = "events_compilation",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    @ToString.Exclude
    @JsonIgnore
    private Set<Event> events;
}