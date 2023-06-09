package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.StateEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationRequestException;
import ru.practicum.request.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.StatusRequest;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public RequestDto createRequest(Long userId, Long eventId) {
        User user = getUser(userId);
        Event event = getEvent(eventId);
        checkRequestIsExist(eventId, userId);
        checkEventOwner(event, userId);
        checkStatePublished(event);
        checkParticipantLimit(event);
        Request request = new Request();
        request.setEvent(event);
        request.setRequester(user);
        if (event.getRequestModeration()) {
            request.setStatus(StatusRequest.PENDING);
        } else {
            request.setStatus(StatusRequest.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
        request.setCreated(LocalDateTime.now());
        log.info("User id {} has created a request to participate in the event id: {}", userId, eventId);
        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {
        getUser(userId);
        Request request = getRequest(requestId);
        if (request.getRequester().getId().equals(userId)) {
            request.setStatus(StatusRequest.CANCELED);
        }
        log.info("Сancelled request id {}", requestId);
        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public EventRequestStatusUpdateResultDto updateRequestsStatus(Long userId, Long eventId,
                                                                  EventRequestStatusUpdateRequestDto
                                                                          eventRequestStatusUpdateRequestDto) {
        getUser(userId);
        Event event = getEvent(eventId);
        EventRequestStatusUpdateResultDto eventRequestStatusUpdateResultDto = new EventRequestStatusUpdateResultDto();
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            return eventRequestStatusUpdateResultDto;
        }
        List<RequestDto> confirmedRequests = new ArrayList<>();
        List<RequestDto> rejectedRequests = new ArrayList<>();
        List<Request> requests = requestRepository.findByEventIdAndStatus(eventId, StatusRequest.PENDING);
        if (requests.size() == 0) {
            throw new ValidationRequestException("Request must have status PENDING.");
        }
        if (eventRequestStatusUpdateRequestDto.getStatus().equals(StatusRequest.REJECTED)) {
            for (Request request : requests) {
                checkStateConfirmed(request);
                request.setStatus(StatusRequest.REJECTED);
                requestRepository.save(request);
                rejectedRequests.add(requestMapper.toRequestDto(request));
            }
        }
        if (eventRequestStatusUpdateRequestDto.getStatus().equals(StatusRequest.CONFIRMED)) {
            if (Objects.equals(Long.valueOf(event.getParticipantLimit()), event.getConfirmedRequests())) {
                throw new ValidationRequestException("Participation limit exceeded.");
            }
            for (Request request : requests) {
                if (event.getParticipantLimit() > event.getConfirmedRequests()) {
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    eventRepository.save(event);
                    request.setStatus(StatusRequest.CONFIRMED);
                    requestRepository.save(request);
                    confirmedRequests.add(requestMapper.toRequestDto(request));
                } else {
                    request.setStatus(StatusRequest.REJECTED);
                    requestRepository.save(request);
                    rejectedRequests.add(requestMapper.toRequestDto(request));
                }
            }
        }
        eventRequestStatusUpdateResultDto.setConfirmedRequests(confirmedRequests);
        eventRequestStatusUpdateResultDto.setRejectedRequests(rejectedRequests);
        log.info("Updated the status of requests {} for the event id {}: ", eventRequestStatusUpdateRequestDto.getRequestIds(),
                eventId);
        return eventRequestStatusUpdateResultDto;
    }

    @Override
    public List<RequestDto> getRequestsByUser(Long userId) {
        getUser(userId);
        List<RequestDto> requestDtos = new ArrayList<>();
        requestRepository.findAllByRequesterId(userId).forEach(r -> requestDtos.add(requestMapper.toRequestDto(r)));
        log.info("List of user id {} requests: {}", userId, requestDtos);
        return requestDtos;
    }

    @Override
    public List<RequestDto> getRequestsForUserEvent(Long userId, Long eventId) {
        getUser(userId);
        Event event = getEvent(eventId);
        List<RequestDto> requestDtos = new ArrayList<>();
        if (event.getInitiator().getId().equals(userId)) {
            requestRepository.findByEventId(eventId).forEach(r -> requestDtos.add(requestMapper.toRequestDto(r)));
            log.info("List of requests for the event id {}: {}", eventId, requestDtos);
        }
        return requestDtos;
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%s was not found", userId)));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id=%s was not found", eventId)));
    }

    private Request getRequest(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException(String.format("Request with id=%s was not found", requestId)));
    }

    private void checkRequestIsExist(Long eventId, Long userId) {
        if (requestRepository.findByEventIdAndRequesterId(eventId, userId).isPresent()) {
            throw new ValidationRequestException("The participation request was not added: " +
                    "you cannot add a repeat request");
        }
    }

    private void checkEventOwner(Event event, Long userId) {
        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidationRequestException("The initiator of the event cannot add a request to participate " +
                    "in his event");
        }
    }

    private void checkParticipantLimit(Event event) {
        if (Objects.equals(Long.valueOf(event.getParticipantLimit()), event.getConfirmedRequests())) {
            throw new ValidationRequestException(String.format("The limit of requests per event %s has been reached",
                    event.getTitle()));
        }
    }

    private void checkStatePublished(Event event) {
        if (!event.getState().equals(StateEvent.PUBLISHED)) {
            throw new ValidationRequestException("You cannot participate in an unpublished event.");
        }
    }

    private void checkStateConfirmed(Request request) {
        if (request.getStatus().equals(StatusRequest.CONFIRMED)) {
            throw new ValidationRequestException("An already accepted application cannot be canceled.");
        }
    }

}
