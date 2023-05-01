package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
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
        Category updatedCategory = getCategoryById(catId);
        Optional.ofNullable(categoryDto.getName()).ifPresent(updatedCategory::setName);
        log.info("Обновлена категория id {}: новое имя {}", catId, updatedCategory.getName());
        return categoryMapper.toCategoryDto(categoryRepository.save(updatedCategory));
    }

    @Override
    public void deleteCategory(Long catId) {
        Category deletedCategory = getCategoryById(catId);
        categoryRepository.deleteById(catId);
        log.info("Удалена категория {}", deletedCategory);
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        categoryRepository.findAll(PageRequest.of(from / size, size))
                .forEach(c -> categoryDtos.add(categoryMapper.toCategoryDto(c)));
        log.info("Получен список категорий " + categoryDtos);
        return categoryDtos;
    }

    @Override
    public CategoryDto getCategory(long catId) {
        return categoryMapper.toCategoryDto(getCategoryById(catId));
    }

    private Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Category with id=%s was not found", id)));
    }

}
