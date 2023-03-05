package ru.practicum.explorewithme.service.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.dto.event.requests.ParticipationRequestDto;
import ru.practicum.explorewithme.model.ParticipationRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParticipationRequestMapper {
    public static ParticipationRequestDto toDto(ParticipationRequest model) {
        return new ParticipationRequestDto(
                model.getCreatedOn(),
                model.getEventId(),
                model.getId(),
                model.getRequesterId(),
                model.getState());
    }
}
