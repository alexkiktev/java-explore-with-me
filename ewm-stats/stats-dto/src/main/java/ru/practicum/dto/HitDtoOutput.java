package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HitDtoOutput {
    private String app;
    private String uri;
    private Long hits;
}
