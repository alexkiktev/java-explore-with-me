package ru.practicum.service;

import ru.practicum.dto.HitDtoInput;
import ru.practicum.dto.HitDtoOutput;

import java.util.List;

public interface StatsService {

    void createHit(HitDtoInput hitDtoInput);

    List<HitDtoOutput> getStats(String start, String end, List<String> uris, Boolean unique);

}
