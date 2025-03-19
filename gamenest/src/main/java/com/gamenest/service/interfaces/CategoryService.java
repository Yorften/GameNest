package com.gamenest.service.interfaces;

import com.gamenest.dto.category.CategoryRequest;
import com.gamenest.dto.category.UpdateCategoryRequest;

import java.util.List;

public interface CategoryService {

    CategoryRequest createCategory(CategoryRequest categoryRequest);

    CategoryRequest updateCategory(Long categoryId, UpdateCategoryRequest categoryRequest);

    CategoryRequest getCategoryById(Long categoryId);

    List<CategoryRequest> getAllCategories();

    void deleteCategory(Long categoryId);
}
