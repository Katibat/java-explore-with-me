package ru.practicum.explorewithme.request.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.explorewithme.request.RequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private Long id;
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private LocalDateTime created;
    private Long eventId;
    private Long requesterId;
    private RequestStatus status;
}