package ru.practicum.explorewithme.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "participation_requests")
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequest {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id")
    private long eventId;

    @Column(name = "requester_id")
    private long requesterId;

    @Column(name = "state")
    @Enumerated(EnumType.ORDINAL)
    private EventRequestStatus state;

    @Column(name = "created_on")
    private LocalDateTime createdOn;
}
