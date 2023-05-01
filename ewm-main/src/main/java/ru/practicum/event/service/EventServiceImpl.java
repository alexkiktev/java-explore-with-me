package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequestDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.StateEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationRequestException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;


    @Override
    public List<EventFullDto> getEvents(List<Long> users, List<StateEvent> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        return null;
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        return null;
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
            Event event = eventMapper.toEvent(newEventDto);
            event.setCategory(categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() ->
                    new NotFoundException(String.format("Category with id=%s was not found", newEventDto.getCategory()))));
            event.setState(StateEvent.PENDING);
            User user = getUser(userId);
            event.setInitiator(user);

            Optional<Location> location = locationRepository.findByLatAndLon(newEventDto.getLocation().getLat(), newEventDto.getLocation().getLon());
            if (location.isPresent()) {
                event.setLocation(location.get());
            } else {
                Location newLocation = new Location(null, newEventDto.getLocation().getLat(), newEventDto.getLocation().getLon());
                event.setLocation(locationRepository.save(newLocation));
            }
            return eventMapper.toEventDto(eventRepository.save(event));
        } else {
            throw new ValidationRequestException(String.format("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: %s", newEventDto.getEventDate()));
        }
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%s was not found", userId)));
    }

}
