package ru.practicum.explorewithme.statistics.dto;

import lombok.Data;

@Data
public class EndpointHit {
    private final Long id;
    private final String app;
    private final String uri;
    private final String ip;
    private final String timestamp;
}
