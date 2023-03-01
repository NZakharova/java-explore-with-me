package ru.practicum.explorewithme.statistics.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.statistics.dto.EndpointHit;
import ru.practicum.explorewithme.statistics.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatService {
    private final EndpointHitRepository repository;

    public EndpointHit registerHit(EndpointHit hit) {
        var model = new EndpointHitModel(null, hit.getApp(), hit.getUri(), hit.getIp(), hit.getTimestamp());

        repository.save(model);

        return new EndpointHit(model.getId(), model.getApp(), model.getUri(), model.getIp(), hit.getTimestamp());
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        return Arrays
                .stream(uris)
                .map(u -> getStatsFor(u, start, end, unique))
                .sorted(Comparator.comparingLong(ViewStats::getHits).reversed())
                .collect(Collectors.toList());
    }

    private ViewStats getStatsFor(String uri, LocalDateTime start, LocalDateTime end, boolean unique) {
        Optional<Long> count = unique
                ? repository.findCountOfDistinctByIpEntriesByUri(uri, start, end)
                : repository.findCountOfEntriesByUri(uri, start, end);

        var app = repository.findTopByUri(uri).orElseThrow();
        return new ViewStats(app.getApp(), uri, count.orElse(0L));
    }
}
