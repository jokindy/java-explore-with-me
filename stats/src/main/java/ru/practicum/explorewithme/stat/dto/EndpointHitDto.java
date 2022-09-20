package ru.practicum.explorewithme.stat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EndpointHitDto {

    private Long id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
