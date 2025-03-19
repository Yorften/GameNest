package com.gamenest.controller;

import com.gamenest.dto.category.CategoryRequest;
import com.gamenest.dto.category.UpdateCategoryRequest;
import com.gamenest.service.interfaces.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Category API", description = "Endpoints for managing categories. Note: Category name must be unique.")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(
        summary = "Create a new category",
        description = "Creates a new category. The category name must be unique."
    )
    @PostMapping
    public ResponseEntity<CategoryRequest> createCategory(
            @Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryRequest createdCategory = categoryService.createCategory(categoryRequest);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Update an existing category",
        description = "Updates an existing category. Only provided fields are updated."
    )
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryRequest> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        CategoryRequest updatedCategory = categoryService.updateCategory(categoryId, updateCategoryRequest);
        return ResponseEntity.ok(updatedCategory);
    }

    @Operation(
        summary = "Get a category by ID",
        description = "Retrieves a single category by its ID."
    )
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryRequest> getCategoryById(@PathVariable Long categoryId) {
        CategoryRequest category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }

    @Operation(
        summary = "Get all categories",
        description = "Retrieves a list of all categories."
    )
    @GetMapping
    public ResponseEntity<List<CategoryRequest>> getAllCategories() {
        List<CategoryRequest> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(
        summary = "Delete a category",
        description = "Deletes a category by its ID."
    )
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
