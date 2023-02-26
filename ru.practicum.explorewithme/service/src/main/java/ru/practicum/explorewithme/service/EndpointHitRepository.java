package ru.practicum.explorewithme.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EndpointHitRepository extends JpaRepository<EndpointHitModel, Long> {
    @Query("SELECT COUNT(DISTINCT(e.ip)) " +
            "FROM EndpointHitModel e " +
            "WHERE e.uri = :uri AND e.timestamp > :start AND e.timestamp < :end ")
    Optional<Long> findCountOfDistinctByIpEntriesByUri(String uri, LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(e) " +
            "FROM EndpointHitModel e " +
            "WHERE e.uri = :uri AND e.timestamp > :start AND e.timestamp < :end")
    Optional<Long> findCountOfEntriesByUri(String uri, LocalDateTime start, LocalDateTime end);

    Optional<EndpointHitModel> findTopByUri(String uri);
}
