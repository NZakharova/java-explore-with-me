package ru.practicum.explorewithme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.event.*;
import ru.practicum.explorewithme.exceptions.ConflictException;
import ru.practicum.explorewithme.exceptions.ObjectNotFoundException;
import ru.practicum.explorewithme.model.*;
import ru.practicum.explorewithme.repository.CategoryRepository;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.UserRepository;
import ru.practicum.explorewithme.service.EventService;
import ru.practicum.explorewithme.service.mappers.EventMapper;
import ru.practicum.explorewithme.utils.DateUtils;
import ru.practicum.explorewithme.utils.TimePeriod;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public EventFullDto get(long id) {
        return EventMapper.toFullDto(getEventModel(id));
    }

    @Override
    public List<EventShortDto> search(String text, List<Long> categories, Boolean paid, TimePeriod period, Boolean onlyAvailable, EventSort sort, Pageable pageable) {
        return toDto(repository.search(text, categories, paid, period, onlyAvailable, sort, pageable));
    }

    @Override
    public List<EventFullDto> getAll(List<Long> users, List<EventState> states, List<Long> categories, TimePeriod timePeriod, Pageable pageable) {
        return toFullDto(repository.search(users, states, categories, timePeriod, pageable));
    }

    @Override
    public EventFullDto patchEvent(long id, UpdateEventAdminRequest event) {
        var model = getEventModel(id);

        updateEvent(event, model);
        updateEventState(event.getStateAction(), model);

        repository.save(model);

        return EventMapper.toFullDto(model);
    }

    @Override
    public List<EventShortDto> getEventsBy(long userId, Pageable pageable) {
        return toDto(repository.findAllByInitiatorId(userId, pageable));
    }

    @Override
    public EventFullDto add(long userId, NewEventDto event, boolean allowOccurred) {
        if (!allowOccurred) {
            validateEventDate(event.getEventDate());
        }

        // требуется обращение к репозиториям, поэтому не использую EventMapper

        var model = new Event(
                null,
                event.getTitle(),
                event.getAnnotation(),
                event.getDescription(),
                getCategoryModel(event.getCategory()),
                getUserModel(userId),
                event.getParticipantLimit(),
                0,
                event.getLocation().getLat(),
                event.getLocation().getLon(),
                event.isPaid(),
                event.isRequestModeration(),
                EventState.PENDING,
                DateUtils.now(),
                event.getEventDate(),
                null
        );

        repository.save(model);

        return EventMapper.toFullDto(model);
    }

    @Override
    public EventFullDto getEvent(long userId, long eventId) {
        return EventMapper.toFullDto(getEventModel(userId, eventId));
    }

    @Override
    public EventFullDto patchEvent(long userId, long eventId, UpdateEventUserRequest event) {
        var model = getEventModel(userId, eventId);

        if (model.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Cannot change published event");
        }

        updateEvent(event, model);
        updateEventState(event.getStateAction(), model);

        repository.save(model);

        return EventMapper.toFullDto(model);
    }

    private void updateEvent(UpdateEventRequestBase event, Event model) {
        if (event.getAnnotation() != null) {
            model.setAnnotation(event.getAnnotation());
        }

        if (event.getCategory() != null) {
            model.setCategory(getCategoryModel(event.getCategory()));
        }

        if (event.getDescription() != null) {
            model.setDescription(event.getDescription());
        }

        if (event.getEventDate() != null) {
            validateEventDate(event.getEventDate());
            model.setEventDate(event.getEventDate());
        }

        if (event.getLocation() != null) {
            model.setLat(event.getLocation().getLat());
            model.setLon(event.getLocation().getLon());
        }

        if (event.getPaid() != null) {
            model.setPaid(event.getPaid());
        }

        if (event.getParticipantLimit() != null) {
            model.setParticipantLimit(event.getParticipantLimit());
        }

        if (event.getRequestModeration() != null) {
            model.setRequestModeration(event.getRequestModeration());
        }

        if (event.getTitle() != null) {
            model.setTitle(event.getTitle());
        }
    }

    private void updateEventState(UserStateAction state, Event model) {
        if (state != null) {
            switch (state) {
                case CANCEL_REVIEW:
                    if (model.getState() != EventState.PENDING) {
                        throw new ConflictException("Event review is already done");
                    }
                    model.setState(EventState.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    model.setState(EventState.PENDING);
                    break;
                default:
                    throw new ConflictException("Invalid value for 'stateAction' field: " + state);
            }
        }
    }

    private void updateEventState(AdminStateAction state, Event model) {
        if (state != null) {
            if (model.getState() != EventState.PENDING) {
                throw new ConflictException("Event review is already done");
            }

            switch (state) {
                case PUBLISH_EVENT:
                    model.setState(EventState.PUBLISHED);
                    break;
                case REJECT_EVENT:
                    model.setState(EventState.CANCELED);
                    break;
                default:
                    throw new ConflictException("Invalid value for 'stateAction' field: " + state);
            }
        }
    }

    private Event getEventModel(long eventId) {
        return repository.findById(eventId).orElseThrow(() -> new ObjectNotFoundException("Event", eventId));
    }

    private Event getEventModel(long userId, long eventId) {
        var model = getEventModel(eventId);
        if (model.getInitiator().getId() != userId) {
            throw new ObjectNotFoundException("Event", eventId);
        }

        return model;
    }

    private void validateEventDate(LocalDateTime date) {
        if (date.plusHours(2).isBefore(DateUtils.now())) {
            throw new ConflictException("EventDate must be at least 2 hours before now");
        }
    }

    private Category getCategoryModel(long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Category", id));
    }

    private User getUserModel(long id) {
        return userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User", id));
    }

    private List<EventShortDto> toDto(List<Event> events) {
        return events.stream().map(EventMapper::toShortDto).collect(Collectors.toList());
    }

    private List<EventFullDto> toFullDto(List<Event> events) {
        return events.stream().map(EventMapper::toFullDto).collect(Collectors.toList());
    }
}
