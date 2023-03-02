package ru.practicum.explorewithme.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.model.EventSort;
import ru.practicum.explorewithme.utils.TimePeriod;

import java.util.List;

public interface EventService {
    EventFullDto get(long id);

    List<EventShortDto> getAll(String text,
                               List<Integer> categories,
                               boolean paid,
                               TimePeriod period,
                               boolean onlyAvailable,
                               EventSort sort,
                               Pageable pageable);
}
