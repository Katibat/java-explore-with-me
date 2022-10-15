package ru.practicum.explorewithme.model.request;

import lombok.*;
import ru.practicum.explorewithme.dto.request.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Запрос на участие в событии
 */

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
    @Column(name = "created")
    private LocalDateTime created;
    private Long eventId;
    @JoinColumn(name = "requester_id")
    private Long requesterId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;
}