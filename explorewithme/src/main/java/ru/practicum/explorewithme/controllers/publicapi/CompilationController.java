package ru.practicum.explorewithme.controllers.publicapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.service.CompilationService;
import ru.practicum.explorewithme.statistics.StatisticsClient;
import ru.practicum.explorewithme.utils.PaginationUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationController {
    private final CompilationService service;
    private final StatisticsClient statistics;

    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size,
                                       HttpServletRequest request) {
        log.info("Public: compilation search");
        statistics.registerHit(request);
        return service.getAll(pinned, PaginationUtils.create(from, size));
    }

    @GetMapping("/{id}")
    public CompilationDto get(@PathVariable long id, HttpServletRequest request) {
        log.info("Public: get compilation {}", id);
        statistics.registerHit(request);
        return service.get(id);
    }
}
