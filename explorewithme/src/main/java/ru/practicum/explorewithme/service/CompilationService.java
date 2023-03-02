package ru.practicum.explorewithme.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getAll(boolean pinned, Pageable pageable);
    CompilationDto get(long id);
}
