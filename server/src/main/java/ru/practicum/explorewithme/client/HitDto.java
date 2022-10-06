package ru.practicum.explorewithme.client;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class HitDto {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    @Builder.Default
    private LocalDateTime timeStamp = LocalDateTime.now();
}