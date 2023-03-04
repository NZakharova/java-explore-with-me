package ru.practicum.explorewithme.statistics.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.statistics.dto.EndpointHit;
import ru.practicum.explorewithme.statistics.dto.ViewStats;
import ru.practicum.explorewithme.statistics.model.DateUtils;
import ru.practicum.explorewithme.statistics.model.StatService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatController {
    private final StatService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHit registerHit(@RequestBody EndpointHit hit) {
        return service.registerHit(hit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam String start,
                                    @RequestParam String end,
                                    @RequestParam String[] uris,
                                    @RequestParam(defaultValue = "false") boolean unique) {
        return service.getStats(DateUtils.parse(start), DateUtils.parse(end), uris, unique);
    }
}
