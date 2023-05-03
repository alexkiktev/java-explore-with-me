package ru.practicum.service;

import ru.practicum.dto.HitDtoInput;
import ru.practicum.dto.HitDtoOutput;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void createHit(HitDtoInput hitDtoInput);

    List<HitDtoOutput> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

}
