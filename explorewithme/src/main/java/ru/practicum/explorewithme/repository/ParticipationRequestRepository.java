package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.EventRequestStatus;
import ru.practicum.explorewithme.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findByEventId(long eventId);
    List<ParticipationRequest> findByRequesterId(long requesterId);

    List<ParticipationRequest> findByEventIdAndState(long eventId, EventRequestStatus state);
}
