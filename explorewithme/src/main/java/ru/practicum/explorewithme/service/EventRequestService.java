package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.event.requests.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.event.requests.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.event.requests.ParticipationRequestDto;

import java.util.List;

public interface EventRequestService {

    List<ParticipationRequestDto> getEventRequests(long userId, long eventId);

    EventRequestStatusUpdateResult patchRequest(long userId, long eventId, EventRequestStatusUpdateRequest updateRequest);

    List<ParticipationRequestDto> getEventRequests(long userId);

    ParticipationRequestDto register(long userId, long eventId);

    ParticipationRequestDto cancel(long userId, long requestId);
}
