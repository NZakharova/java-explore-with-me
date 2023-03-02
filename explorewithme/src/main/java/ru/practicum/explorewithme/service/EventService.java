package ru.practicum.explorewithme.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.model.EventSort;
import ru.practicum.explorewithme.model.EventState;
import ru.practicum.explorewithme.utils.TimePeriod;

import java.util.List;

public interface EventService {
    EventFullDto get(long id);

    List<EventShortDto> search(String text,
                               List<Integer> categories,
                               boolean paid,
                               TimePeriod period,
                               boolean onlyAvailable,
                               EventSort sort,
                               Pageable pageable);

    List<EventShortDto> getAll(List<Long> users, List<EventState> states, List<Long> categories, TimePeriod timePeriod, Pageable pageable);

    EventRequestStatusUpdateResult patch(long id, EventRequestStatusUpdateRequest event);
}
