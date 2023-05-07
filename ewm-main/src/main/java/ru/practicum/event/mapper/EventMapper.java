package ru.practicum.event.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventMapper {

    public Event toEvent(@NotNull NewEventDto newEventDto) {
        Event event = new Event();
        event.setTitle(newEventDto.getTitle());
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setCreatedOn(LocalDateTime.now());
        event.setPublishedOn(LocalDateTime.now());
        event.setPaid(newEventDto.getPaid());
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        return event;
    }

    public EventFullDto toEventDto(Event event, List<CommentShortDto> comments) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setCategory(event.getCategory());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setState(event.getState());
        eventFullDto.setLocation(event.getLocation());
        eventFullDto.setInitiator(event.getInitiator());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setViews(0L);
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setConfirmedRequests(0L);
        eventFullDto.setComments(comments);
        return eventFullDto;
    }

    public EventShortDto toEventShortDto(Event event, List<CommentShortDto> commentShortDtos) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(event.getCategory());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setInitiator(event.getInitiator());
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setViews(event.getViews());
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
        eventShortDto.setComments(commentShortDtos);
        return eventShortDto;
    }

}
