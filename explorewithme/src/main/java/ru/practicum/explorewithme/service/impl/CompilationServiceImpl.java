package ru.practicum.explorewithme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explorewithme.exceptions.ObjectNotFoundException;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.repository.CompilationRepository;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.service.CompilationService;
import ru.practicum.explorewithme.service.mappers.CompilationMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final EventRepository eventRepository;

    @Override
    public List<CompilationDto> getAll(Boolean pinned, Pageable pageable) {
        if (pinned != null) {
            return repository.findAllByPinned(pinned, pageable).stream().map(CompilationMapper::toDto).collect(Collectors.toList());
        }
        else {
            return repository.findAll(pageable).stream().map(CompilationMapper::toDto).collect(Collectors.toList());
        }
    }

    @Override
    public CompilationDto get(long id) {
        return CompilationMapper.toDto(getModel(id));
    }

    @Override
    public CompilationDto add(NewCompilationDto dto) {
        var events = eventRepository.findAllById(dto.getEvents());
        var model = CompilationMapper.toModel(dto, events);
        return CompilationMapper.toDto(repository.save(model));
    }

    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }

    @Override
    public CompilationDto patch(long id, UpdateCompilationRequest dto) {
        var model = getModel(id);

        if (dto.getPinned() != null) {
            model.setPinned(dto.getPinned());
        }

        if (dto.getTitle() != null) {
            model.setTitle(dto.getTitle());
        }

        if (dto.getEvents() != null) {
            var events = eventRepository.findAllById(dto.getEvents());
            model.setEvents(events);
        }

        repository.save(model);

        return CompilationMapper.toDto(model);
    }

    private Compilation getModel(long id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Compilation", id));
    }
}
