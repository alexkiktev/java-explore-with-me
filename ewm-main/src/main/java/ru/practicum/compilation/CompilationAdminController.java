package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationNewDto;
import ru.practicum.compilation.dto.CompilationUpdateDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody CompilationNewDto compilationNewDto) {
        log.info("Received a POST request to create a collection of events {}", compilationNewDto);
        return compilationService.createCompilation(compilationNewDto);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilation(@PathVariable(name = "compId") Long compilationId,
                                            @RequestBody CompilationUpdateDto compilationUpdateDto) {
        log.info("Received a PATCH request to update information about the collection id {}: {}", compilationId,
                compilationUpdateDto);
        return compilationService.updateCompilation(compilationId, compilationUpdateDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable(name = "compId") Long compilationId) {
        log.info("A DELETE request was received to delete the collection id {}", compilationId);
        compilationService.deleteCompilation(compilationId);
    }

}
