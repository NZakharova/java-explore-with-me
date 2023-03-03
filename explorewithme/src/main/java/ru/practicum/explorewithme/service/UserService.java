package ru.practicum.explorewithme.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.dto.event.requests.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.event.requests.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.event.requests.ParticipationRequestDto;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;

import java.util.List;

public interface UserService {
    List<UserShortDto> getAll(List<Long> ids, Pageable pageable);

    UserDto add(NewUserRequest user);

    void delete(long id);

    List<EventShortDto> getEvents(long id, Pageable pageable);

    EventFullDto createEvent(long id, NewEventDto event);

    EventFullDto getEvent(long id, long eventId);

    EventFullDto patch(long id, long eventId, NewEventDto event);

    List<ParticipationRequestDto> getEventRequests(long id, long eventId);

    EventRequestStatusUpdateResult patchRequests(long id, long eventId, EventRequestStatusUpdateRequest updateRequest);

    List<ParticipationRequestDto> getEventRequests(long id);

    ParticipationRequestDto register(long id, long eventId);

    ParticipationRequestDto cancel(long id, long requestId);
}
