package ru.practicum.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    @NotBlank(message = "Field: name. Error: must not be blank. Value: null")
    private String name;
}
