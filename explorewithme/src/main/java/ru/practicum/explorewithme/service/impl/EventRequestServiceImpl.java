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
