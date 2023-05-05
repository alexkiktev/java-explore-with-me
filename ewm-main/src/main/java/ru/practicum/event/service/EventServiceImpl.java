package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.client.StatsMapper;
import ru.practicum.event.dto.*;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.*;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationRequestException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final StatsClient statsClient;
    private final StatsMapper statsMapper;

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        if (!newEventDto.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
            throw new ValidationRequestException(String.format("Field: eventDate. Error: the event cannot be earlier " +
                    "than two hours from the current moment. Value: %s", newEventDto.getEventDate()));
        }
        Event event = createDataEvent(newEventDto, userId);
        log.info("User id {} has created new event: {}", userId, event);
        return eventMapper.toEventDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto updateEventAdmin(Long eventId, UpdateEventDto updateEventDto) {
        Event event = getEvent(eventId);
        if (updateEventDto.getEventDate() != null) {
            checkTimeEvent(updateEventDto, 1);
            event.setEventDate(updateEventDto.getEventDate());
        }
        if (updateEventDto.getStateAction() != null) {
            if (updateEventDto.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                checkStateEventIsPublishedOrCanceled(event);
                event.setState(StateEvent.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                event.setViews(0L);
            } else {
                checkStateEventIsPublishedOrCanceled(event);
                event.setState(StateEvent.CANCELED);
            }
        }
        updateDataEvent(updateEventDto, event);
        log.info("Admin has updated the event id {}: new data {}", eventId, event);
        return eventMapper.toEventDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventDto updateEventDto) {
        Event event = getEvent(eventId);
        checkEventOwner(event, userId);
        checkStateEventIsPublished(event);
        if (updateEventDto.getEventDate() != null) {
            checkTimeEvent(updateEventDto, 2);
            event.setEventDate(updateEventDto.getEventDate());
        }
        if (updateEventDto.getStateAction() != null) {
            if ((event.getState().equals(StateEvent.PENDING)) || (event.getState().equals(StateEvent.CANCELED))) {
                if (updateEventDto.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                    event.setState(StateEvent.PENDING);
                } else {
                    event.setState(StateEvent.CANCELED);
                }
            } else {
                throw new ValidationRequestException("Only pending or canceled events can be changed");
            }
        }
        updateDataEvent(updateEventDto, event);
        log.info("User id {} has updated event id {}: {}", userId, eventId, event);
        return eventMapper.toEventDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> getEventsByUser(Long userId, Integer from, Integer size) {
        getUser(userId);
        List<EventFullDto> eventFullDtos = new ArrayList<>();
        Pageable pageParams = PageRequest.of(from / size, size);
        eventRepository.findByInitiatorId(userId, pageParams)
                .forEach(e -> eventFullDtos.add(eventMapper.toEventDto(e)));
        log.info("List of events created by the user: {}", eventFullDtos);
        return eventFullDtos;
    }

    @Override
    public EventFullDto getEventByUserAndId(Long userId, Long eventId) {
        getUser(userId);
        Event event = getEvent(eventId);
        log.info("Received event id {} created by the user id: {}", eventId, userId);
        return eventMapper.toEventDto(event);
    }

    @Override
    public List<EventFullDto> getEventsAdmin(List<Long> users, List<StateEvent> states, List<Long> categories,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from,
                                             Integer size) {
        Pageable pageParams = PageRequest.of(from / size, size);
        List<EventFullDto> eventFullDtos = new ArrayList<>();
        Specification<Event> eventCriterias = EventCriteriaQuery.getFilterEventsAdmin(users, states, categories,
                rangeStart, rangeEnd);
        eventRepository.findAll(eventCriterias,
                pageParams).getContent().forEach(e -> eventFullDtos.add(eventMapper.toEventDto(e)));
        log.info("The admin searched for events according to the specified criteria");
        return eventFullDtos;
    }

    @Override
    public List<EventShortDto> getEventsWithParameters(String text, List<Long> categories, Boolean paid,
                                                       LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                       Boolean onlyAvailable, SortParam sort, Integer from,
                                                       Integer size, HttpServletRequest request) {
        Pageable pageParams = PageRequest.of(from / size, size);
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        Specification<Event> eventCriterias = EventCriteriaQuery.getFilterEventsPublic(text, categories, paid,
                rangeStart, rangeEnd, sort, StateEvent.PUBLISHED, onlyAvailable);
        Page<Event> eventsPage = eventRepository.findAll(eventCriterias, pageParams);
        List<Event> events = eventsPage.getContent();
        for (Event event : events) {
            event.setViews(event.getViews() + 1);
            eventRepository.save(event);
            eventShortDtos.add(eventMapper.toEventShortDto(event));
        }
        statsClient.createHit(statsMapper.toHitCreateDto(request));
        log.info("The result of the event search by the specified criteria: {}", eventShortDtos);
        return eventShortDtos;
    }

    @Override
    public EventFullDto getEventByIdPublic(Long id, HttpServletRequest request) {
        Event event = eventRepository.findEventByIdAndState(id, StateEvent.PUBLISHED);
        if (event == null) {
            throw new NotFoundException(String.format("Event with id=%s was not found", id));
        }
        event.setViews(event.getViews() + 1);
        eventRepository.save(event);
        statsClient.createHit(statsMapper.toHitCreateDto(request));
        log.info("Event search result by id: {}", event);
        return eventMapper.toEventDto(event);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%s was not found", userId)));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id=%s was not found", eventId)));
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException(String.format("Category with id=%s was not found", categoryId)));
    }

    private Location getOrCreateLocation(LocationDto locationDto) {
        Optional<Location> location = locationRepository.findByLatAndLon(locationDto.getLat(), locationDto.getLon());
        if (location.isPresent()) {
            return location.get();
        } else {
            Location newLocation = new Location(null, locationDto.getLat(), locationDto.getLon());
            return locationRepository.save(newLocation);
        }
    }

    private void updateDataEvent(UpdateEventDto updateEventDto, Event event) {
        Optional.ofNullable(updateEventDto.getTitle()).ifPresent(event::setTitle);
        Optional.ofNullable(updateEventDto.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(updateEventDto.getDescription()).ifPresent(event::setDescription);
        if (updateEventDto.getCategory() != null) {
            event.setCategory(getCategory(updateEventDto.getCategory()));
        }
        if (updateEventDto.getLocation() != null) {
            event.setLocation(getOrCreateLocation(updateEventDto.getLocation()));
        }
        Optional.ofNullable(updateEventDto.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(updateEventDto.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(updateEventDto.getRequestModeration()).ifPresent(event::setRequestModeration);
    }

    private Event createDataEvent(NewEventDto newEventDto, Long userId) {
        Event event = eventMapper.toEvent(newEventDto);
        event.setCategory(getCategory(newEventDto.getCategory()));
        event.setState(StateEvent.PENDING);
        User user = getUser(userId);
        event.setInitiator(user);
        event.setLocation(getOrCreateLocation(newEventDto.getLocation()));
        event.setConfirmedRequests(0L);
        return event;
    }

    private void checkTimeEvent(UpdateEventDto updateEventDto, Integer countHours) {
        if (!updateEventDto.getEventDate().isAfter(LocalDateTime.now().plusHours(countHours))) {
            throw new ValidationRequestException(String.format("Field: eventDate. Error: the event cannot be " +
                    "earlier than one hours from the current moment. Value: %s", updateEventDto.getEventDate()));
        }
    }

    private void checkEventOwner(Event event, Long userId) {
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationRequestException(String.format("Only the owner can change the event id %s",
                    event.getId()));
        }
    }

    private void checkStateEventIsPublishedOrCanceled(Event event) {
        if ((event.getState().equals(StateEvent.PUBLISHED)) || (event.getState().equals(StateEvent.CANCELED))) {
            throw new ValidationRequestException(String.format("Cannot publish the event because it's not in " +
                    "the right state: %s", event.getState()));
        }
    }

    private void checkStateEventIsPublished(Event event) {
        if (event.getState().equals(StateEvent.PUBLISHED)) {
            throw new ValidationRequestException(String.format("Cannot publish the event because it's not in " +
                    "the right state: %s", event.getState()));
        }
    }

}
