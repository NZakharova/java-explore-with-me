package ru.practicum.explorewithme.controllers.adminapi;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.event.*;
import ru.practicum.explorewithme.model.EventState;
import ru.practicum.explorewithme.service.EventService;
import ru.practicum.explorewithme.statistics.StatisticsClient;
import ru.practicum.explorewithme.utils.DateUtils;
import ru.practicum.explorewithme.utils.PaginationUtils;
import ru.practicum.explorewithme.utils.TimePeriod;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventsController {
    private final StatisticsClient statistics;
    private final EventService service;

    @GetMapping
    public List<EventShortDto> getAll(@RequestParam List<Long> users,
                                      @RequestParam List<EventState> states,
                                      @RequestParam List<Long> categories,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = DateUtils.DATE_FORMAT) LocalDateTime rangeStart,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = DateUtils.DATE_FORMAT) LocalDateTime rangeEnd,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size,
                                      HttpServletRequest request) {
        statistics.registerHit(request);
        return service.getAll(users, states, categories, new TimePeriod(rangeStart, rangeEnd), PaginationUtils.create(from, size));
    }

    @PatchMapping("/{id}")
    public EventRequestStatusUpdateResult patch(@PathVariable long id, @RequestBody EventRequestStatusUpdateRequest event, HttpServletRequest request) {
        statistics.registerHit(request);
        return service.patch(id, event);
    }
}
