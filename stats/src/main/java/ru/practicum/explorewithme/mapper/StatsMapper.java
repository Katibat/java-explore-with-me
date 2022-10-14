package ru.practicum.explorewithme.mapper;

import lombok.*;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.model.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatsMapper {

    public static EndpointHit toModel(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .id(endpointHitDto.getId())
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(endpointHitDto.getTimestamp())
                .build();
    }
}