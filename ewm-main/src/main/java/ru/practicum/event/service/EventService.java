package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.model.SortParam;
import ru.practicum.event.model.StateEvent;
import ru.practicum.exception.ValidationRequestException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventFullDto> getEventsAdmin(List<Long> users, List<StateEvent> states, List<Long> categories,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto updateEventAdmin(Long eventId, UpdateEventDto updateEventDto);

    EventFullDto createEvent(Long userId, NewEventDto newEventDto) throws ValidationRequestException;

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventDto updateEventDto);

    List<EventFullDto> getEventsByUser(Long userId, Integer from, Integer size);

    EventFullDto getEventByUserAndId(Long userId, Long eventId);

    List<EventShortDto> getEventsWithParameters(String text, List<Long> categories, Boolean paid,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                Boolean onlyAvailable, SortParam sort, Integer from,
                                                Integer size, HttpServletRequest request);

    EventFullDto getEventByIdPublic(Long id, HttpServletRequest request);

}
