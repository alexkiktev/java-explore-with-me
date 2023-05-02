package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.StateEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationRequestException;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.StatusRequest;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public RequestDto createRequest(Long userId, Long eventId) {
        User user = getUser(userId);
        Event event = getEvent(eventId);
        if (requestRepository.findByEventIdAndRequesterId(eventId, userId).isPresent()) {
            throw new ValidationRequestException("Запрос на участие не был добавлен: нельзя добавить повторный запрос");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidationRequestException("The initiator of the event cannot add a request to participate in his event");
        }
        if (!event.getState().equals(StateEvent.PUBLISHED)) {
            throw new ValidationRequestException("You cannot participate in an unpublished event.");
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ValidationRequestException(String.format("The limit of requests per event %s has been reached", event.getTitle()));
        }
        Request request = new Request();
        request.setEvent(event);
        request.setRequester(user);
        request.setStatus(event.getRequestModeration() ? StatusRequest.PENDING : StatusRequest.CONFIRMED);
        request.setCreated(LocalDateTime.now());
        log.info("Пользователем {} создана заявка на участие в событии: {}", userId, eventId);
        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%s was not found", userId)));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id=%s was not found", eventId)));
    }

}
