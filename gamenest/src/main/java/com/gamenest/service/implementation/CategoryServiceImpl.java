package com.gamenest.service.implementation;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gamenest.dto.category.CategoryRequest;
import com.gamenest.dto.category.UpdateCategoryRequest;
import com.gamenest.exception.DuplicateResourceException;
import com.gamenest.exception.ResourceNotFoundException;
import com.gamenest.mapper.CategoryMapper;
import com.gamenest.model.Category;
import com.gamenest.repository.CategoryRepository;
import com.gamenest.service.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryRequest createCategory(CategoryRequest categoryRequest) {
        categoryRepository.findByName(categoryRequest.getName())
            .ifPresent(existingCategory -> {
                throw new DuplicateResourceException("Category with name " + categoryRequest.getName() + " already exists");
            });
        
        Category category = categoryMapper.convertToEntity(categoryRequest);
        category = categoryRepository.save(category);
        return categoryMapper.convertToDTO(category);
    }
    @Override
    public CategoryRequest updateCategory(Long categoryId, UpdateCategoryRequest categoryRequest) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (categoryRequest.getName() != null && !categoryRequest.getName().isEmpty()) {
            existingCategory.setName(categoryRequest.getName());
        }
        if (categoryRequest.getDescription() != null && !categoryRequest.getDescription().isEmpty()) {
            existingCategory.setDescription(categoryRequest.getDescription());
        }
        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.convertToDTO(updatedCategory);
    }

    @Override
    public CategoryRequest getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return categoryMapper.convertToDTO(category);
    }

    @Override
    public List<CategoryRequest> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        categoryRepository.delete(category);
    }
}
