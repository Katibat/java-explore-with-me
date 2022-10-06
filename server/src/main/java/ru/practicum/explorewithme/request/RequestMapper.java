package ru.practicum.explorewithme.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.request.model.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {

    public static RequestFullDto toFullDto(Request request) {
        return RequestFullDto.builder()
                .id(request.getId())
                .eventId(request.getEventId())
                .requesterId(request.getRequesterId())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }
}