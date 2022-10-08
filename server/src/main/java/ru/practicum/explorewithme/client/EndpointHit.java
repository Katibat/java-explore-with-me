package ru.practicum.explorewithme.client;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EndpointHit {
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;

    public EndpointHit(String app, String uri, String ip) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = LocalDateTime.now();
    }
}