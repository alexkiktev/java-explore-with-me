package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDtoInput;
import ru.practicum.dto.HitDtoOutput;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public void createHit(@RequestBody @Valid HitDtoInput hitDtoInput) {
        log.info("К эндпоинту был запрос {}", hitDtoInput);
        statsService.createHit(hitDtoInput);
    }

    @GetMapping("/stats")
    public List<HitDtoOutput> getStats(@RequestParam(name = "start") String start,
                                      @RequestParam(name = "end") String end,
                                      @RequestParam(name = "uris", required = false) List<String> uris,
                                      @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        log.info("Получение статистики с параметрами: start = {}, end = {}, uris = {}, unique ip = {}",
                start, end, uris, unique);
        return statsService.getStats(start, end, uris, unique);
    }

}
