package ru.practicum.client;

import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDtoInput;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class StatsMapper {

    public HitDtoInput toHitCreateDto(HttpServletRequest request) {
        HitDtoInput hitDtoInput = new HitDtoInput();
        hitDtoInput.setApp("ewm-service");
        hitDtoInput.setUri(request.getRequestURI());
        hitDtoInput.setIp(request.getRemoteAddr());
        hitDtoInput.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return hitDtoInput;
    }

}
