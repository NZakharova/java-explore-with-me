package ru.practicum.explorewithme.controllers.adminapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explorewithme.service.CompilationService;
import ru.practicum.explorewithme.statistics.StatisticsClient;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationsController {
    private final StatisticsClient statistics;
    private final CompilationService service;

    @PostMapping
    public CompilationDto create(@RequestBody @Validated NewCompilationDto dto, HttpServletRequest request) {
        log.info("Admin: create compilation: " + dto);
        statistics.registerHit(request);
        var result = service.add(dto);
        log.info("Admin: compilation created: " + result);
        return result;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id, HttpServletRequest request) {
        log.info("Admin: delete compilation: " + id);
        statistics.registerHit(request);
        service.delete(id);
    }

    @PatchMapping("/{id}")
    public CompilationDto patch(@PathVariable long id, @RequestBody @Validated UpdateCompilationRequest dto, HttpServletRequest request) {
        log.info("Admin: patch compilation: " + id + ", value" + dto);
        statistics.registerHit(request);
        return service.patch(id, dto);
    }
}
