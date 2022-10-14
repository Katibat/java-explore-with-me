package ru.practicum.explorewithme.mapper;

import lombok.*;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.request.RequestDto;
import ru.practicum.explorewithme.model.request.Request;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {

    public RequestDto toDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEventId())
                .requester(request.getRequesterId())
                .status(request.getStatus())
                .build();
    }
}