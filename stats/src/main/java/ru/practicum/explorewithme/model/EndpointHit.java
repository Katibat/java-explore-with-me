package ru.practicum.explorewithme.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Эндроинт к сервису статистики
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "stats")
public class EndpointHit {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Идентификатор записи
    private String app; // Идентификатор сервиса для которого записывается информация
    private String uri; // URI для которого был осуществлен запрос
    private String ip; // IP-адрес пользователя, осуществившего запрос
    private LocalDateTime timestamp; // Дата и время, когда был совершен запрос к эндпоинту
}