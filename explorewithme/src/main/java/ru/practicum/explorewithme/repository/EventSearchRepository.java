package ru.practicum.explorewithme.repository;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.EventSort;
import ru.practicum.explorewithme.model.EventState;
import ru.practicum.explorewithme.utils.TimePeriod;

import java.util.List;

public interface EventSearchRepository {
    List<Event> search(String text, List<Long> categories, Boolean paid, TimePeriod period, Boolean onlyAvailable, EventSort sort, Pageable pageable);
    List<Event> search(List<Long> users, List<EventState> states, List<Long> categories, TimePeriod timePeriod, Pageable pageable);
}
