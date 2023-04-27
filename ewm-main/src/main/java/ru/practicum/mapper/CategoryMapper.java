package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.CategoryDto;
import ru.practicum.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(CategoryDto categoryDto);

    CategoryDto toCategoryDto(Category category);

}
