package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.HitDtoInput;
import ru.practicum.model.Hit;

@Mapper(componentModel = "spring")
public interface StatsMapper {

    @Mapping(target = "timestamp", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Hit toHit(HitDtoInput hitDto);

}
