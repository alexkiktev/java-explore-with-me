package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class CompilationNewDto {
    @NotNull
    private Boolean pinned;
    @NotBlank
    private String title;
    private List<Long> events;
}
