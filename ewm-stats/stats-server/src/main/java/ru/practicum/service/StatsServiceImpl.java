package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDtoInput;
import ru.practicum.dto.HitDtoOutput;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final StatsMapper statsMapper;

    @Override
    public void createHit(HitDtoInput hitDtoInput) {
        statsRepository.save(statsMapper.toHit(hitDtoInput));
    }

    @Override
    public List<HitDtoOutput> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (unique) {
            if (uris == null || uris.isEmpty()) {
                return statsRepository.getHitsWithUniqueIpAndAllUris(start, end.plusDays(10));
            } else {
                return statsRepository.getHitsWithUniqueIp(start, end.plusDays(10), uris);
            }
        } else {
            if (uris == null || uris.isEmpty()) {
                return statsRepository.getHitsAllUris(start, end.plusDays(10));
            } else {
                return statsRepository.getHits(start, end.plusDays(10), uris);
            }
        }
    }

}
