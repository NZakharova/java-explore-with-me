package ru.practicum.explorewithme.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getAll(Boolean pinned, Pageable pageable);
    CompilationDto get(long id);

    CompilationDto add(NewCompilationDto dto);

    void delete(long id);

    CompilationDto patch(long id, UpdateCompilationRequest dto);
}
