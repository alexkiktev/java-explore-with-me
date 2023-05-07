package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.SortParam;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventPublicController {

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsPublic(@RequestParam(name = "text", required = false) String text,
                                               @RequestParam(name = "categories",
                                                       required = false) List<Long> categories,
                                               @RequestParam(name = "paid", required = false) Boolean paid,
                                               @RequestParam(name = "rangeStart", required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                   LocalDateTime rangeStart,
                                               @RequestParam(name = "rangeEnd", required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                   LocalDateTime rangeEnd,
                                               @RequestParam(name = "onlyAvailable",
                                                       required = false, defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(name = "sort", required = false) SortParam sort,
                                               @PositiveOrZero @RequestParam(name = "from",
                                                       defaultValue = "0") Integer from,
                                               @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                               HttpServletRequest request) {
        log.info("Received a GET request for events by parameters");
        return eventService.getEventsWithParameters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventShortDto getEventByIdPublic(@PathVariable Long id, HttpServletRequest request) {
        log.info("Event GET request received by id {}", id);
        return eventService.getEventByIdPublic(id, request);
    }

}
