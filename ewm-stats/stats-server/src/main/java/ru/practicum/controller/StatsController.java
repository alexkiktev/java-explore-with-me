package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDtoInput;
import ru.practicum.dto.HitDtoOutput;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHit(@RequestBody @Valid HitDtoInput hitDtoInput) {
        log.info("К эндпоинту был запрос {}", hitDtoInput);
        statsService.createHit(hitDtoInput);
    }

    @GetMapping("/stats")
    public List<HitDtoOutput> getStats(@RequestParam(name = "start")
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                      @RequestParam(name = "end")
                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                      @RequestParam(name = "uris", required = false) List<String> uris,
                                      @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        log.info("Получение статистики с параметрами: start = {}, end = {}, uris = {}, unique ip = {}",
                start, end, uris, unique);
        return statsService.getStats(start, end, uris, unique);
    }

}
