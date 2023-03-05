package ru.practicum.explorewithme.controllers.privateapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.dto.comment.UpdateCommentRequest;
import ru.practicum.explorewithme.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.dto.event.requests.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.event.requests.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.event.requests.ParticipationRequestDto;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.service.CommentService;
import ru.practicum.explorewithme.service.EventRequestService;
import ru.practicum.explorewithme.service.EventService;
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
    private final EventService eventService;
    private final EventRequestService eventRequestService;
    private final CommentService commentService;

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
    @ResponseStatus(HttpStatus.CREATED)
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
    @ResponseStatus(HttpStatus.CREATED)
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

    // пользователь может получать все свои комментарии
    @GetMapping("/{id}/comments")
    public List<CommentDto> getComments(@PathVariable long id,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size,
                                        HttpServletRequest request) {
        log.info("Users {}: get comments", id);
        statistics.registerHit(request);
        return commentService.getUserComments(id, PaginationUtils.create(from, size));
    }

    // пользователь может оставлять комментарии
    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto postComment(@PathVariable long id, @RequestBody NewCommentDto newComment, HttpServletRequest request) {
        log.info("Users {}: post comment: {}", id, newComment);
        statistics.registerHit(request);
        return commentService.createComment(id, newComment);
    }

    // пользователь может обновлять комментарий, пока он не прошёл проверку
    @PatchMapping("/{id}/comments/{commentId}")
    public CommentDto updateComment(@PathVariable long id, @PathVariable long commentId, @RequestBody UpdateCommentRequest updateComment, HttpServletRequest request) {
        log.info("Users {}: update comment {} with {}", id, commentId, request);
        statistics.registerHit(request);
        return commentService.updateComment(id, commentId, updateComment);
    }

    // пользователь может отменять публикацию своего комменатрия, пока он не прошёл проверку
    @PatchMapping("/{id}/comments/{commentId}/cancel")
    public CommentDto cancelComment(@PathVariable long id, @PathVariable long commentId, HttpServletRequest request) {
        log.info("Users {}: cancel comment {}", id, commentId);
        statistics.registerHit(request);
        return commentService.cancelComment(id, commentId);
    }

    // пользователь может удалять свои комменатрии
    @DeleteMapping("/{id}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long id, @PathVariable long commentId, HttpServletRequest request) {
        log.info("Users {}: delete comment {}", id, commentId);
        statistics.registerHit(request);
        commentService.deleteComment(id, commentId);
    }
}
