package ru.practicum.explorewithme.utils;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class TimePeriod {
    private final LocalDateTime start;
    private final LocalDateTime end;
}
