package ru.practicum.explorewithme.event.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.event.EventState;
import ru.practicum.explorewithme.event.location.Location;
import ru.practicum.explorewithme.request.RequestStatus;
import ru.practicum.explorewithme.request.model.Request;
import ru.practicum.explorewithme.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 5, max = 128)
    @Column(name = "title")
    private String title;

    @Size(min = 5, max = 512)
    @Column(name = "annotation")
    private String annotation;

    @Size(min = 5, max = 1024)
    @Column(name = "description")
    private String description;

    @JoinColumn(name = "category_id")
    private Long categoryId;

    @Column(name = "created_on", insertable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdOn;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @JoinColumn(name = "initiator_id")
    private Long initiatorId;

    @Column(name = "paid")
    private boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private EventState state;

    @Embedded
    private Location location;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @ToString.Exclude
    private List<Request> requests;

    @ManyToMany(mappedBy = "events")
    @ToString.Exclude
    private Set<Compilation> compilations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", insertable = false, updatable = false, nullable = false)
    @ToString.Exclude
    private User initiator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false, nullable = false)
    @ToString.Exclude
    private Category category;

    public Long getConfirmedRequests() {
        return requests.stream()
                .filter(request -> request.getStatus().equals(RequestStatus.CONFIRMED))
                .count();
    }
}