package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequestDto;
import ru.practicum.event.model.StateEvent;
import ru.practicum.exception.ValidationRequestException;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventFullDto> getEvents(List<Long> users, List<StateEvent> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto);

    EventFullDto createEvent(Long userId, NewEventDto newEventDto) throws ValidationRequestException;

}
