package ru.practicum.explorewithme.controllers.adminapi;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explorewithme.service.CompilationService;
import ru.practicum.explorewithme.statistics.StatisticsClient;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationsController {
    private final StatisticsClient statistics;
    private final CompilationService service;

    @PostMapping
    public CompilationDto create(@RequestBody NewCompilationDto dto, HttpServletRequest request) {
        statistics.registerHit(request);
        return service.add(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id, HttpServletRequest request) {
        statistics.registerHit(request);
        service.delete(id);
    }

    @PatchMapping("/{id}")
    public CompilationDto patch(@PathVariable long id, @RequestBody UpdateCompilationRequest dto, HttpServletRequest request) {
        statistics.registerHit(request);
        return service.patch(id, dto);
    }
}
