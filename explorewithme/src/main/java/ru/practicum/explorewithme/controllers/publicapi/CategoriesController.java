package ru.practicum.explorewithme.controllers.publicapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.service.CategoryService;
import ru.practicum.explorewithme.statistics.StatisticsClient;
import ru.practicum.explorewithme.utils.PaginationUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriesController {
    private final CategoryService service;
    private final StatisticsClient statistics;

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "10") int size,
                                    HttpServletRequest request) {
        log.info("Public: category search");
        statistics.registerHit(request);
        return service.getAll(PaginationUtils.create(from, size));
    }

    @GetMapping("/{id}")
    public CategoryDto get(@PathVariable long id, HttpServletRequest request) {
        log.info("Public: get category {}", id);
        statistics.registerHit(request);
        return service.get(id);
    }
}
