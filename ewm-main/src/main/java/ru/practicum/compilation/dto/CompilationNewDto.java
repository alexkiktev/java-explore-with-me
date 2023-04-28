package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.event.model.Event;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
public class CompilationNewDto {
    private Boolean pinned;
    @NotBlank
    private String title;
    private List<Event> events;
}
