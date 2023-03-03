package ru.practicum.explorewithme.controllers.adminapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.event.*;
import ru.practicum.explorewithme.dto.event.requests.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.model.EventState;
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
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventsController {
    private final StatisticsClient statistics;
    private final EventService service;

    @GetMapping
    public List<EventShortDto> getAll(@RequestParam(required = false) List<Long> users,
                                      @RequestParam(required = false) List<EventState> states,
                                      @RequestParam(required = false) List<Long> categories,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = DateUtils.DATE_FORMAT) LocalDateTime rangeStart,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = DateUtils.DATE_FORMAT) LocalDateTime rangeEnd,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size,
                                      HttpServletRequest request) {
        log.info("Admin: event search");
        statistics.registerHit(request);
        return service.getAll(users, states, categories, new TimePeriod(rangeStart, rangeEnd), PaginationUtils.create(from, size));
    }

    @PatchMapping("/{id}")
    public EventFullDto patch(@PathVariable long id, @RequestBody @Validated UpdateEventAdminRequest event, HttpServletRequest request) {
        log.info("Admin: event patch: " + id + ", value: " + event);
        statistics.registerHit(request);
        return service.patchEvent(id, event);
    }
}
