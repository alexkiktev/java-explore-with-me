package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationNewDto;
import ru.practicum.compilation.dto.CompilationUpdateDto;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(CompilationNewDto compilationNewDto);

    CompilationDto updateCompilation(Long compId, CompilationUpdateDto compilationUpdateDto);

    void deleteCompilation(Long compId);

    CompilationDto getCompilationById(Long compId);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

}
