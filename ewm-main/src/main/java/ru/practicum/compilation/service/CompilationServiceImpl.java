package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
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
        log.info("Created compilation {}", compilationDto);
        return compilationDto;
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compilationId, CompilationUpdateDto compilationUpdateDto) {
        Compilation updatedCompilation = getCompilation(compilationId);
        Optional.ofNullable(compilationUpdateDto.getTitle()).ifPresent(updatedCompilation::setTitle);
        Optional.ofNullable(compilationUpdateDto.getPinned()).ifPresent(updatedCompilation::setPinned);
        if (compilationUpdateDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllByIdIn(compilationUpdateDto.getEvents());
            updatedCompilation.setEvents(events);
        }
        log.info("Updated compilation id {}: new data is {}", compilationId, updatedCompilation);
        return compilationMapper.toCompilationDto(compilationRepository.save(updatedCompilation));
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compilationId) {
        getCompilation(compilationId);
        compilationRepository.deleteById(compilationId);
        log.info("Removed compilation id {}", compilationId);
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(Long compilationId) {
        return compilationMapper.toCompilationDto(getCompilation(compilationId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        List<CompilationDto> compilationDtos = new ArrayList<>();
        Pageable pageParams = PageRequest.of(from / size, size);
        compilationRepository.findAllByPinnedIs(pinned, pageParams)
                .forEach(c -> compilationDtos.add(compilationMapper.toCompilationDto(c)));
        log.info("Received a list of compilations {}", compilationDtos);
        return compilationDtos;
    }

    private Compilation getCompilation(Long id) {
        return compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Compilation with id=%s was not found", id)));
    }

}