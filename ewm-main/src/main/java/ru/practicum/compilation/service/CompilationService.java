package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationNewDto;
import ru.practicum.compilation.dto.CompilationUpdateDto;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(CompilationNewDto compilationNewDto);

    CompilationDto updateCompilation(Long compilationId, CompilationUpdateDto compilationUpdateDto);

    void deleteCompilation(Long compilationId);

    CompilationDto getCompilationById(Long compilationId);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

}
