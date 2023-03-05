package ru.practicum.explorewithme.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationUtils {
    public static Pageable create(int start, int size) {
        return PageRequest.of(start / size, size);
    }
}
