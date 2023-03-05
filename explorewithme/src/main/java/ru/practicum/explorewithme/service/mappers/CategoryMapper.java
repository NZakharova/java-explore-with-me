package ru.practicum.explorewithme.service.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.utils.DateUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static Category toModel(NewCategoryDto dto) {
        return new Category(null, dto.getName(), DateUtils.now());
    }
}
