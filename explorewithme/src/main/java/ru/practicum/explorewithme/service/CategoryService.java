package ru.practicum.explorewithme.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAll(Pageable pageable);

    CategoryDto get(long id);

    CategoryDto add(NewCategoryDto category);

    void delete(long id);

    CategoryDto patch(long id, NewCategoryDto category);
}
