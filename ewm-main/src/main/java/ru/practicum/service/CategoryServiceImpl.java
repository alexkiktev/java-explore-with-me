package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CategoryDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        CategoryDto createdCategoryDto = categoryMapper.toCategoryDto(categoryRepository
                .save(categoryMapper.toCategory(categoryDto)));
        log.info("Создана категория {}", createdCategoryDto);
        return createdCategoryDto;
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long catId) {
        Category updatedCategory = getCategory(catId);
        Optional.ofNullable(categoryDto.getName()).ifPresent(updatedCategory::setName);
        log.info("Обновлена категория id {}: новое имя {}", catId, updatedCategory.getName());
        return categoryMapper.toCategoryDto(categoryRepository.save(updatedCategory));
    }

    @Override
    public void deleteCategory(Long catId) {
        Category deletedCategory = getCategory(catId);
        categoryRepository.deleteById(catId);
        log.info("Удалена категория {}", deletedCategory);
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Category with id=%s was not found", id)));
    }

}
