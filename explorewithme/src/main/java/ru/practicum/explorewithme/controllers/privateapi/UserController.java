package ru.practicum.explorewithme.controllers.privateapi;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.event.requests.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.event.requests.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.event.requests.ParticipationRequestDto;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.service.UserService;
import ru.practicum.explorewithme.statistics.StatisticsClient;
import ru.practicum.explorewithme.utils.PaginationUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final StatisticsClient statistics;
    private final UserService service;

    @GetMapping("/{id}/events")
    public List<EventShortDto> getEvents(@PathVariable long id,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size,
                                         HttpServletRequest request) {
        statistics.registerHit(request);
        return service.getEvents(id, PaginationUtils.create(from, size));
    }

    @PostMapping("/{id}/events")
    public EventFullDto createEvent(@PathVariable long id, @RequestBody NewEventDto event, HttpServletRequest request) {
        statistics.registerHit(request);
        return service.createEvent(id, event);
    }

    @GetMapping("{id}/events/{eventId}")
    public EventFullDto getEvent(@PathVariable long id, @PathVariable long eventId, HttpServletRequest request) {
        statistics.registerHit(request);
        return service.getEvent(id, eventId);
    }

    @PatchMapping("/{id}/events/{eventId}")
    public EventFullDto patchEvent(@PathVariable long id, @PathVariable long eventId, @RequestBody NewEventDto event, HttpServletRequest request) {
        statistics.registerHit(request);
        return service.patch(id, eventId, event);
    }

    @GetMapping("/{id}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@PathVariable long id,
                                                          @PathVariable long eventId,
                                                          HttpServletRequest request) {
        statistics.registerHit(request);
        return service.getEventRequests(id, eventId);
    }

    @PatchMapping("/{id}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult patchRequests(@PathVariable long id,
                                                        @PathVariable long eventId,
                                                        @RequestBody EventRequestStatusUpdateRequest update,
                                                        HttpServletRequest request) {
        statistics.registerHit(request);
        return service.patchRequests(id, eventId, update);
    }

    @GetMapping("/{id}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable long id, HttpServletRequest request) {
        statistics.registerHit(request);
        return service.getEventRequests(id);
    }

    @PostMapping("/{id}/requests")
    public ParticipationRequestDto createRequest(@PathVariable long id, @RequestParam long eventId, HttpServletRequest request) {
        statistics.registerHit(request);
        return service.register(id, eventId);
    }

    @PatchMapping("/{id}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable long id, @PathVariable long requestId, HttpServletRequest request) {
        statistics.registerHit(request);
        return service.cancel(id, requestId);
    }
}
