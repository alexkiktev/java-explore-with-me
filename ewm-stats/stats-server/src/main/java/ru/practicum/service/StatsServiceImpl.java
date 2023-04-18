package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDtoInput;
import ru.practicum.dto.HitDtoOutput;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.Hit;
import ru.practicum.repository.StatsRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public List<HitDtoOutput> getStats(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime startDateTime = getDateFromString(start);
        LocalDateTime endDateTime = getDateFromString(end);
        if (unique) {
            if (uris == null || uris.isEmpty()) {
                return statsRepository.getHitsWithUniqueIpAndAllUris(startDateTime, endDateTime);
            } else {
                return statsRepository.getHitsWithUniqueIp(startDateTime, endDateTime, uris);
            }
        } else {
            if (uris == null || uris.isEmpty()) {
                return statsRepository.getHitsAllUris(startDateTime, endDateTime);
            } else {
                return statsRepository.getHits(startDateTime, endDateTime, uris);
            }
        }
    }

    private LocalDateTime getDateFromString(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }

}
