package ru.practicum.explorewithme.request.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.explorewithme.request.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests")
public class Request {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "event_id")
    private Long eventId;
    @JoinColumn(name = "requester_id")
    private Long requesterId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;
    @CreationTimestamp
    @Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();
}