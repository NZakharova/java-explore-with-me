package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.statistics.dto.DateUtils;
import ru.practicum.explorewithme.statistics.dto.EndpointHit;
import ru.practicum.explorewithme.statistics.dto.ViewStats;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatController {
    private final StatService service;

    @PostMapping("/hit")
    public EndpointHit registerHit(@RequestBody EndpointHit hit) {
        return service.registerHit(hit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam String start,
                                    @RequestParam String end,
                                    @RequestParam String[] uris,
                                    @RequestParam boolean unique) {
        return service.getStats(DateUtils.parse(start), DateUtils.parse(end), uris, unique);
    }
}
