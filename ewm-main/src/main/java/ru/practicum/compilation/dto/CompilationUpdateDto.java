package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CompilationUpdateDto {
    private Boolean pinned;
    private String title;
    private List<Long> events;
}
