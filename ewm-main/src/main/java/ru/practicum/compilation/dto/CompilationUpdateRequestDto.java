package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.event.model.Event;

import java.util.List;

@Data
@AllArgsConstructor
public class CompilationUpdateRequestDto {
    private Boolean pinned;
    private String title;
    private List<Event> events;
}
