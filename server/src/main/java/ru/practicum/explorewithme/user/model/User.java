package ru.practicum.explorewithme.user.model;

import lombok.*;
import ru.practicum.explorewithme.event.model.Event;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 256)
    @Column(name = "name")
    private String name;
    @Size(max = 512)
    @Column(name = "email", unique = true)
    private String email;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "initiator_id")
    private Set<Event> events;
}