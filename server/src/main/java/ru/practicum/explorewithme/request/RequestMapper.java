package ru.practicum.explorewithme.request;

import lombok.*;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.request.model.*;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {

    public RequestDto toDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .eventId(request.getEventId())
                .requesterId(request.getRequesterId())
                .status(request.getStatus())
                .build();
    }
}