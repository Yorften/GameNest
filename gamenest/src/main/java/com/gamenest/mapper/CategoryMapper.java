package com.gamenest.mapper;

import org.springframework.stereotype.Component;
import com.gamenest.dto.category.CategoryRequest;
import com.gamenest.model.Category;

@Component
public class CategoryMapper {

    public Category convertToEntity(CategoryRequest categoryRequest) {
        if (categoryRequest == null) {
            return null;
        }
        return Category.builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .build();
    }

    public CategoryRequest convertToDTO(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryRequest.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
