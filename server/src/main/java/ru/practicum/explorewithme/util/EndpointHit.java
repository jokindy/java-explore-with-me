package ru.practicum.explorewithme.util;

import lombok.Data;
import lombok.NoArgsConstructor;

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
