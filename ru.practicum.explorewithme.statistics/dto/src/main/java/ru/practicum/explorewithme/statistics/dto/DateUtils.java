package ru.practicum.explorewithme.statistics.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime parse(String date) {
        return LocalDateTime.parse(date, dateTimeFormatter);
    }

    public static String format(LocalDateTime date) {
        return dateTimeFormatter.format(date);
    }
}
