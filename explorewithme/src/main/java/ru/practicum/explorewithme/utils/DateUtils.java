package ru.practicum.explorewithme.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static LocalDateTime parse(String date) {
        return LocalDateTime.parse(date, dateTimeFormatter);
    }

    public static String format(LocalDateTime date) {
        return dateTimeFormatter.format(date);
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
}
