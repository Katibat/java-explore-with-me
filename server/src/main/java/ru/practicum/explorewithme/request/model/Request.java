package ru.practicum.explorewithme.request.model;

import lombok.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "event_id")
    private Long event;
    @JoinColumn(name = "requester_id")
    private Long requester;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;
    @Column(name = "created")
    private LocalDateTime created;
}