package ru.practicum.explorewithme.statistics.dto;

import lombok.Data;

@Data
public class ViewStats {
    private final String app;
    private final String uri;
    private final long hits;
}
