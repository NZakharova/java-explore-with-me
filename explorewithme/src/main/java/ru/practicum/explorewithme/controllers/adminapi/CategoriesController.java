package ru.practicum.explorewithme.controllers.adminapi;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.service.CategoryService;
import ru.practicum.explorewithme.statistics.StatisticsClient;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoriesController {
    private final StatisticsClient statistics;
    private final CategoryService service;

    @PostMapping
    public CategoryDto create(@RequestBody NewCategoryDto category, HttpServletRequest request) {
        statistics.registerHit(request);
        return service.add(category);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id, HttpServletRequest request) {
        statistics.registerHit(request);
        service.delete(id);
    }

    @PatchMapping("/{id}")
    public CategoryDto patch(@PathVariable long id, @RequestBody NewCategoryDto category, HttpServletRequest request) {
        statistics.registerHit(request);
        return service.patch(id, category);
    }
}
