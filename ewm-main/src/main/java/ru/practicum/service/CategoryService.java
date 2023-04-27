package ru.practicum.service;

import ru.practicum.dto.CategoryDto;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, Long catId);

    void deleteCategory(Long catId);

}
