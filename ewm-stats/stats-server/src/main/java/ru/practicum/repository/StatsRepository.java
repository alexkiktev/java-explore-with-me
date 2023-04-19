package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.HitDtoOutput;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {

    @Query("SELECT new ru.practicum.dto.HitDtoOutput(h.app, h.uri, COUNT (DISTINCT h.ip)) " +
            "FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :startDateTime AND :endDateTime " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.ip) DESC")
    List<HitDtoOutput> getHitsWithUniqueIpAndAllUris(@Param("startDateTime") LocalDateTime startDateTime,
                                                     @Param("endDateTime") LocalDateTime endDateTime);

    @Query("SELECT new ru.practicum.dto.HitDtoOutput(h.app, h.uri, COUNT (DISTINCT h.ip)) " +
            "FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :startDateTime AND :endDateTime " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.ip) DESC")
    List<HitDtoOutput> getHitsWithUniqueIp(@Param("startDateTime") LocalDateTime startDateTime,
                                           @Param("endDateTime") LocalDateTime endDateTime,
                                           @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.dto.HitDtoOutput(h.app, h.uri, COUNT (h.ip)) " +
            "FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :startDateTime AND :endDateTime " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.ip) DESC")
    List<HitDtoOutput> getHitsAllUris(@Param("startDateTime") LocalDateTime startDateTime,
                                      @Param("endDateTime") LocalDateTime endDateTime);

    @Query("SELECT new ru.practicum.dto.HitDtoOutput(h.app, h.uri, COUNT (h.ip)) " +
            "FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :startDateTime AND :endDateTime " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.ip) DESC")
    List<HitDtoOutput> getHits(@Param("startDateTime") LocalDateTime startDateTime,
                               @Param("endDateTime") LocalDateTime endDateTime,
                               @Param("uris") List<String> uris);

}
