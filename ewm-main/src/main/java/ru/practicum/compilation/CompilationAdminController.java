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
        log.info("Получен POST-запрос на создание подборки событий {}", compilationNewDto);
        return compilationService.createCompilation(compilationNewDto);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @RequestBody CompilationUpdateDto compilationUpdateDto) {
        log.info("Получен PATCH-запрос на обновление информации о подборке id {}: {}", compId, compilationUpdateDto);
        return compilationService.updateCompilation(compId, compilationUpdateDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Получен DELETE-запрос на удаление подборки id {}", compId);
        compilationService.deleteCompilation(compId);
    }

}
