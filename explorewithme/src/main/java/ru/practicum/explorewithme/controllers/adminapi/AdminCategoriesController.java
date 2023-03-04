package ru.practicum.explorewithme.controllers.adminapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.service.CategoryService;
import ru.practicum.explorewithme.statistics.StatisticsClient;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoriesController {
    private final StatisticsClient statistics;
    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody @Validated NewCategoryDto category, HttpServletRequest request) {
        log.info("Admin: create category: {}", category);
        statistics.registerHit(request);
        var dto = service.add(category);
        log.info("Admin: category created: {}", dto);
        return dto;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id, HttpServletRequest request) {
        log.info("Admin: delete category {}", id);
        statistics.registerHit(request);
        service.delete(id);
    }

    @PatchMapping("/{id}")
    public CategoryDto patch(@PathVariable long id, @RequestBody @Validated NewCategoryDto category, HttpServletRequest request) {
        log.info("Admin: patch category {}, value: {}", id, category);
        statistics.registerHit(request);
        return service.patch(id, category);
    }
}
