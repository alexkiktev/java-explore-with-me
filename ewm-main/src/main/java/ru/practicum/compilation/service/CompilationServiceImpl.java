package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationNewDto;
import ru.practicum.compilation.dto.CompilationUpdateDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    @Override
    public CompilationDto createCompilation(CompilationNewDto compilationNewDto) {
        if (compilationNewDto.getEvents() == null) {
            compilationNewDto.setEvents(new ArrayList<>());
        }
        List<Event> events = eventRepository.findAllByIdIn(compilationNewDto.getEvents());
        if (events.size() != compilationNewDto.getEvents().size()) {
            throw new BadRequestException("Field: events. A nonexistent event is specified.");
        }
        CompilationDto compilationDto = compilationMapper
                .toCompilationDto(compilationRepository.save(compilationMapper.toCompilation(compilationNewDto)));
        log.info("Создана подборка {}", compilationDto);
        return compilationDto;
    }

    @Override
    public CompilationDto updateCompilation(Long compId, CompilationUpdateDto compilationUpdateDto) {
        Compilation updatedCompilation = getCompilation(compId);
        Optional.ofNullable(compilationUpdateDto.getTitle()).ifPresent(updatedCompilation::setTitle);
        Optional.ofNullable(compilationUpdateDto.getPinned()).ifPresent(updatedCompilation::setPinned);
        if (compilationUpdateDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllByIdIn(compilationUpdateDto.getEvents());
            updatedCompilation.setEvents(events);
        }
        log.info("Обновлена подборка id {}: новые данные {}", compId, updatedCompilation);
        return compilationMapper.toCompilationDto(compilationRepository.save(updatedCompilation));
    }

    @Override
    public void deleteCompilation(Long compId) {
        getCompilation(compId);
        compilationRepository.deleteById(compId);
        log.info("Удалена подборка id {}", compId);
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        return compilationMapper.toCompilationDto(getCompilation(compId));
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        List<CompilationDto> compilationDtos = new ArrayList<>();
        Pageable pageParams = PageRequest.of(from / size, size);
        compilationRepository.findAllByPinnedIs(pinned, pageParams)
                .forEach(c -> compilationDtos.add(compilationMapper.toCompilationDto(c)));
        return compilationDtos;
    }

    private Compilation getCompilation(Long id) {
        return compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Compilation with id=%s was not found", id)));
    }

}
