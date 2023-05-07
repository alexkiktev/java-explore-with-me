package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.model.Category;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.StateEvent;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    private Long id;
    private String title;
    private String annotation;
    private String description;
    private Category category;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private StateEvent state;
    private User initiator;
    private Location location;
    private Boolean paid;
    private Boolean requestModeration;
    private Long views;
    private Integer participantLimit;
    private Long confirmedRequests;
    private List<CommentShortDto> comments;
}
