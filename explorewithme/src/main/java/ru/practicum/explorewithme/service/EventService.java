package ru.practicum.explorewithme.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.dto.event.*;
import ru.practicum.explorewithme.model.EventSort;
import ru.practicum.explorewithme.model.EventState;
import ru.practicum.explorewithme.utils.TimePeriod;

import java.util.List;

public interface EventService {
    EventFullDto get(long id);

    List<EventShortDto> search(String text,
                               List<Long> categories,
                               Boolean paid,
                               TimePeriod period,
                               Boolean onlyAvailable,
                               EventSort sort,
                               Pageable pageable);

    List<EventFullDto> getAll(List<Long> users, List<EventState> states, List<Long> categories, TimePeriod timePeriod, Pageable pageable);

    EventFullDto patchEvent(long id, UpdateEventAdminRequest event);

    List<EventShortDto> getEventsBy(long userId, Pageable pageable);

    EventFullDto add(long userId, NewEventDto event, boolean allowOccurred);

    EventFullDto getEvent(long userId, long eventId);

    EventFullDto patchEvent(long userId, long eventId, UpdateEventUserRequest event);
}
