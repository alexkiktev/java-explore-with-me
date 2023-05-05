package ru.practicum.request.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.Request;

@Service
public class RequestMapper {

    public RequestDto toRequestDto(Request request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setStatus(request.getStatus());
        requestDto.setCreated(request.getCreated());
        return requestDto;
    }

}