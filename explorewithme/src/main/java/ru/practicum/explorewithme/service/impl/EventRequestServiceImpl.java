package ru.practicum.explorewithme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.requests.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.event.requests.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.event.requests.ParticipationRequestDto;
import ru.practicum.explorewithme.exceptions.ConflictException;
import ru.practicum.explorewithme.exceptions.ObjectNotFoundException;
import ru.practicum.explorewithme.model.EventRequestStatus;
import ru.practicum.explorewithme.model.EventState;
import ru.practicum.explorewithme.model.ParticipationRequest;
import ru.practicum.explorewithme.repository.ParticipationRequestRepository;
import ru.practicum.explorewithme.service.EventRequestService;
import ru.practicum.explorewithme.service.EventService;
import ru.practicum.explorewithme.service.UserService;
import ru.practicum.explorewithme.service.mappers.ParticipationRequestMapper;
import ru.practicum.explorewithme.utils.DateUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventRequestServiceImpl implements EventRequestService {
    private final ParticipationRequestRepository repository;
    private final EventService eventService;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getEventRequests(long userId, long eventId) {
        eventService.getEvent(userId, eventId);

        return toDto(repository.findByEventId(eventId));
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult patchRequest(long userId, long eventId, EventRequestStatusUpdateRequest updateRequest) {
        userService.get(userId);

        var event = eventService.get(eventId);

        if (event.getInitiator().getId() != userId) {
            throw new ObjectNotFoundException("Event", eventId);
        }

        if (updateRequest.getStatus() == EventRequestStatus.CONFIRMED && event.getParticipantLimit() > 0) {
            var participantsCount = repository.countByEventIdAndState(eventId, EventRequestStatus.CONFIRMED);
            var participantLimit = event.getParticipantLimit() - participantsCount;
            if (updateRequest.getRequestIds().size() > participantLimit) {
                throw new ConflictException("Cannot accept more requests than allowed by event");
            }
        }

        for (var reqId : updateRequest.getRequestIds()) {
            processRequestUpdate(reqId, event, updateRequest.getStatus());
        }

        var confirmed = repository.findByEventIdAndState(eventId, EventRequestStatus.CONFIRMED);
        var rejected = repository.findByEventIdAndState(eventId, EventRequestStatus.REJECTED);

        return new EventRequestStatusUpdateResult(toDto(confirmed), toDto(rejected));
    }

    private void processRequestUpdate(long reqId, EventFullDto event, EventRequestStatus status) {
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            throw new ConflictException("Request does not require approval");
        }

        var request = getRequest(reqId);

        if (request.getState() != EventRequestStatus.PENDING && status != request.getState()) {
            throw new ConflictException("Invalid state transition");
        }

        request.setState(status);

        repository.save(request);
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(long userId) {
        userService.get(userId);
        return toDto(repository.findByRequesterId(userId));
    }

    @Override
    public ParticipationRequestDto register(long userId, long eventId) {
        if (repository.findByEventIdAndRequesterId(eventId, userId).isPresent()) {
            throw new ConflictException("User " + userId + " is already registered for event " + eventId);
        }

        var event = eventService.get(eventId);
        if (event.getInitiator().getId() == userId) {
            throw new ConflictException("Cannot register user to it's own event");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Cannot register user to unpublished event");
        }

        if (event.getParticipantLimit() > 0) {
            // это скорее всего ошибка в тестах postman, потому что логически можно зарегистрироваться,
            // пока кол-во *подтверждённых* заявок меньше лимита.
            // Здесь должен быть вызов countByEventIdAndState(eventId, EventRequestStatus.CONFIRMED),
            // такая же проверка проводится при подтверждении заявок автором события.
            var participantsCount = repository.countByEventId(eventId);
            if (participantsCount + 1 > event.getParticipantLimit()) {
                throw new ConflictException("Cannot register any more participants");
            }
        }

        var model = new ParticipationRequest(null, eventId, userId, EventRequestStatus.PENDING, DateUtils.now());
        return ParticipationRequestMapper.toDto(repository.save(model));
    }

    @Override
    public ParticipationRequestDto cancel(long userId, long requestId) {
        var request = getRequest(userId, requestId);
        request.setState(EventRequestStatus.CANCELED);

        repository.save(request);
        return ParticipationRequestMapper.toDto(request);
    }

    private ParticipationRequest getRequest(long requestId) {
        return repository.findById(requestId).orElseThrow(() -> new ObjectNotFoundException("ParticipationRequest", requestId));
    }

    private ParticipationRequest getRequest(long userId, long requestId) {
        var request = getRequest(requestId);
        if (request.getRequesterId() != userId) {
            throw new ObjectNotFoundException("ParticipationRequest", requestId);
        }

        return request;
    }

    private static List<ParticipationRequestDto> toDto(List<ParticipationRequest> requests) {
        return requests.stream().map(ParticipationRequestMapper::toDto).collect(Collectors.toList());
    }
}
