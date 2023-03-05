package ru.practicum.explorewithme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.exceptions.ObjectNotFoundException;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.repository.CategoryRepository;
import ru.practicum.explorewithme.service.CategoryService;
import ru.practicum.explorewithme.service.mappers.CategoryMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Override
    public List<CategoryDto> getAll(Pageable pageable) {
        return repository.findAll(pageable).stream().map(CategoryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto get(long id) {
        return CategoryMapper.toDto(getModel(id));
    }

    @Override
    public CategoryDto add(NewCategoryDto category) {
        var model = CategoryMapper.toModel(category);
        return CategoryMapper.toDto(repository.save(model));
    }

    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }

    @Override
    public CategoryDto patch(long id, NewCategoryDto category) {
        var model = getModel(id);

        model.setName(category.getName());

        repository.save(model);

        return CategoryMapper.toDto(model);
    }

    private Category getModel(long id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Category", id));
    }
}
