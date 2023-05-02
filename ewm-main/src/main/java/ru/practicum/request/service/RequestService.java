package ru.practicum.request.service;

import ru.practicum.request.dto.RequestDto;

public interface RequestService {
    RequestDto createRequest(Long userId, Long eventId);

}
