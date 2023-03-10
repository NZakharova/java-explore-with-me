package ru.practicum.explorewithme.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.utils.DateUtils;
import ru.practicum.explorewithme.statistics.dto.EndpointHit;
import ru.practicum.explorewithme.statistics.dto.ViewStats;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class StatisticsClient extends BaseClient {
    @Autowired
    public StatisticsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public EndpointHit registerHit(HttpServletRequest request) {
        return registerHit(new EndpointHit(
                null,
                "ewm-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                DateUtils.now()));
    }

    public EndpointHit registerHit(EndpointHit hit) {
        return post("/hit", EndpointHit.class, hit).getBody();
    }

    public List<ViewStats> getStats(String[] uris, LocalDateTime start, LocalDateTime end, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", DateUtils.format(start),
                "end", DateUtils.format(end),
                "unique", Boolean.toString(unique),
                "uris", uris
        );
        var response = get("/stats?start={start}&end={end}&unique={unique}&uris={uris}", ViewStats[].class, parameters).getBody();
        if (response == null) {
            throw new NoSuchElementException();
        }
        return Arrays.asList(response);
    }
}
