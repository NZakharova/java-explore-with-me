package ru.practicum.explorewithme.controllers.privateapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.dto.event.requests.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.event.requests.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.event.requests.ParticipationRequestDto;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.service.EventRequestService;
import ru.practicum.explorewithme.service.EventService;
import ru.practicum.explorewithme.service.UserService;
import ru.practicum.explorewithme.statistics.StatisticsClient;
import ru.practicum.explorewithme.utils.PaginationUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final StatisticsClient statistics;
    private final UserService userService;
    private final EventService eventService;
    private final EventRequestService eventRequestService;

    @GetMapping("/{id}/events")
    public List<EventShortDto> getEvents(@PathVariable long id,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size,
                                         HttpServletRequest request) {
        log.info("Users {}: get events", id);
        statistics.registerHit(request);
        return eventService.getEventsBy(id, PaginationUtils.create(from, size));
    }

    @PostMapping("/{id}/events")
    public EventFullDto createEvent(@PathVariable long id, @RequestBody @Validated NewEventDto event, HttpServletRequest request) {
        log.info("Users {}: create event: {}", id, event);
        statistics.registerHit(request);
        var result = eventService.add(id, event);
        log.info("Users {}: created event: {}", id, result);
        return result;
    }

    @GetMapping("/{id}/events/{eventId}")
    public EventFullDto getEvent(@PathVariable long id, @PathVariable long eventId, HttpServletRequest request) {
        log.info("Users {}: get event: {}", id, eventId);
        statistics.registerHit(request);
        return eventService.getEvent(id, eventId);
    }

    @PatchMapping("/{id}/events/{eventId}")
    public EventFullDto patchEvent(@PathVariable long id, @PathVariable long eventId, @RequestBody @Validated UpdateEventUserRequest event, HttpServletRequest request) {
        log.info("Users {}: patch event {}: {}", id, eventId, event);
        statistics.registerHit(request);
        return eventService.patchEvent(id, eventId, event);
    }

    @GetMapping("/{id}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@PathVariable long id,
                                                          @PathVariable long eventId,
                                                          HttpServletRequest request) {
        log.info("Users {}: get events {} requests", id, eventId);
        statistics.registerHit(request);
        return eventRequestService.getEventRequests(id, eventId);
    }

    @PatchMapping("/{id}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult patchRequests(@PathVariable long id,
                                                        @PathVariable long eventId,
                                                        @RequestBody @Validated EventRequestStatusUpdateRequest update,
                                                        HttpServletRequest request) {
        log.info("Users {}: patch events {} requests: {}", id, eventId, update);
        statistics.registerHit(request);
        return eventRequestService.patchRequest(id, eventId, update);
    }

    @GetMapping("/{id}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable long id, HttpServletRequest request) {
        log.info("Users {}: get requests", id);
        statistics.registerHit(request);
        return eventRequestService.getEventRequests(id);
    }

    @PostMapping("/{id}/requests")
    public ParticipationRequestDto createRequest(@PathVariable long id, @RequestParam long eventId, HttpServletRequest request) {
        log.info("Users {}: create request for event {}", id, eventId);
        statistics.registerHit(request);
        var result = eventRequestService.register(id, eventId);
        log.info("Users {}: create request for event {} result: {}", id, eventId, result);
        return result;
    }

    @PatchMapping("/{id}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable long id, @PathVariable long requestId, HttpServletRequest request) {
        log.info("Users {}: cancel request {}", id, requestId);
        statistics.registerHit(request);
        return eventRequestService.cancel(id, requestId);
    }
}
