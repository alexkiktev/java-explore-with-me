package ru.practicum.request.service;

import ru.practicum.request.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto createRequest(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);

    List<RequestDto> getRequestsByUser(Long userId);

    EventRequestStatusUpdateResultDto updateRequestsStatus(Long userId, Long eventId,
                                                           EventRequestStatusUpdateRequestDto
                                                                   eventRequestStatusUpdateRequestDto);

    List<RequestDto> getRequestsForUserEvent(Long userId, Long eventId);

}
