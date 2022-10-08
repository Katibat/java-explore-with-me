package ru.practicum.explorewithme.request;

import lombok.*;
import ru.practicum.explorewithme.request.model.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {

    public static RequestDto toDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .event(request.getEvent())
                .requester(request.getRequester())
                .status(request.getStatus().toString())
                .created(request.getCreated())
                .build();
    }
}