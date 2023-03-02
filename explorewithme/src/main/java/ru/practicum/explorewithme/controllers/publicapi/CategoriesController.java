package ru.practicum.explorewithme.controllers.publicapi;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.service.CategoryService;
import ru.practicum.explorewithme.statistics.StatisticsClient;
import ru.practicum.explorewithme.utils.PaginationUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        statistics.registerHit(request);
        return service.getAll(PaginationUtils.create(from, size));
    }

    @GetMapping("/{id}")
    public CategoryDto get(@PathVariable long id, HttpServletRequest request) {
        statistics.registerHit(request);
        return service.get(id);
    }
}
