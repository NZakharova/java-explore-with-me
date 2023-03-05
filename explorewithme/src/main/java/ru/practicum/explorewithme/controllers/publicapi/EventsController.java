package ru.practicum.explorewithme.controllers.publicapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.model.EventSort;
import ru.practicum.explorewithme.service.EventService;
import ru.practicum.explorewithme.statistics.StatisticsClient;
import ru.practicum.explorewithme.utils.DateUtils;
import ru.practicum.explorewithme.utils.PaginationUtils;
import ru.practicum.explorewithme.utils.TimePeriod;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventsController {
    private final EventService service;
    private final StatisticsClient statistics;

    @GetMapping
    public List<EventShortDto> getAll(@RequestParam(required = false) String text,
                                      @RequestParam(required = false) List<Long> categories,
                                      @RequestParam(required = false) Boolean paid,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = DateUtils.DATE_FORMAT) LocalDateTime rangeStart,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = DateUtils.DATE_FORMAT) LocalDateTime rangeEnd,
                                      @RequestParam(required = false) Boolean onlyAvailable,
                                      @RequestParam(required = false) EventSort sort,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size,
                                      HttpServletRequest request) {
        log.info("Public: search event");
        statistics.registerHit(request);
        return service.search(text, categories, paid, new TimePeriod(rangeStart, rangeEnd), onlyAvailable, sort, PaginationUtils.create(from, size));
    }

    @GetMapping("/{id}")
    public EventFullDto get(@PathVariable long id, HttpServletRequest request) {
        log.info("Public: get event {}", id);
        statistics.registerHit(request);
        return service.get(id);
    }
}
