package unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.gamenest.dto.category.CategoryRequest;
import com.gamenest.dto.category.UpdateCategoryRequest;
import com.gamenest.exception.DuplicateResourceException;
import com.gamenest.exception.ResourceNotFoundException;
import com.gamenest.mapper.CategoryMapper;
import com.gamenest.model.Category;
import com.gamenest.repository.CategoryRepository;
import com.gamenest.service.implementation.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    private CategoryServiceImpl categoryService;

    private Category testCategory;
    private CategoryRequest testCategoryRequest;
    private UpdateCategoryRequest testUpdateCategoryRequest;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepository, categoryMapper);

        testCategory = Category.builder()
                .id(1L)
                .name("Action")
                .description("Action games")
                .build();

        testCategoryRequest = CategoryRequest.builder()
                .id(1L)
                .name("Action")
                .description("Action games")
                .build();

        testUpdateCategoryRequest = UpdateCategoryRequest.builder()
                .name("Adventure")
                .description("Adventure games")
                .build();
    }

    @Test
    void createCategory_WhenCategoryDoesNotExist_ShouldCreateAndReturnDTO() {
        when(categoryRepository.findByName(testCategoryRequest.getName())).thenReturn(Optional.empty());
        when(categoryMapper.convertToEntity(testCategoryRequest)).thenReturn(testCategory);
        when(categoryRepository.save(testCategory)).thenReturn(testCategory);
        when(categoryMapper.convertToDTO(testCategory)).thenReturn(testCategoryRequest);

        CategoryRequest result = categoryService.createCategory(testCategoryRequest);

        assertNotNull(result);
        assertEquals(testCategoryRequest.getName(), result.getName());
        verify(categoryRepository).findByName(testCategoryRequest.getName());
        verify(categoryRepository).save(testCategory);
    }

    @Test
    void createCategory_WhenCategoryExists_ShouldThrowDuplicateResourceException() {
        when(categoryRepository.findByName(testCategoryRequest.getName())).thenReturn(Optional.of(testCategory));

        assertThrows(DuplicateResourceException.class, () -> categoryService.createCategory(testCategoryRequest));
        verify(categoryRepository).findByName(testCategoryRequest.getName());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void updateCategory_WhenCategoryExists_ShouldUpdateAndReturnDTO() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.save(testCategory)).thenReturn(testCategory);
        when(categoryMapper.convertToDTO(testCategory)).thenReturn(testCategoryRequest);

        CategoryRequest result = categoryService.updateCategory(1L, testUpdateCategoryRequest);

        assertNotNull(result);
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(testCategory);
    }

    @Test
    void updateCategory_WhenCategoryDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.updateCategory(1L, testUpdateCategoryRequest));
        verify(categoryRepository).findById(1L);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void getCategoryById_WhenCategoryExists_ShouldReturnDTO() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(categoryMapper.convertToDTO(testCategory)).thenReturn(testCategoryRequest);

        CategoryRequest result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        verify(categoryRepository).findById(1L);
    }

    @Test
    void getCategoryById_WhenCategoryDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(1L));
        verify(categoryRepository).findById(1L);
    }

    @Test
    void getAllCategories_ShouldReturnListOfDTOs() {
        List<Category> categoryList = List.of(testCategory);
        when(categoryRepository.findAll()).thenReturn(categoryList);
        when(categoryMapper.convertToDTO(testCategory)).thenReturn(testCategoryRequest);

        List<CategoryRequest> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(categoryRepository).findAll();
    }

    @Test
    void deleteCategory_WhenCategoryExists_ShouldDeleteCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        categoryService.deleteCategory(1L);

        verify(categoryRepository).findById(1L);
        verify(categoryRepository).delete(testCategory);
    }

    @Test
    void deleteCategory_WhenCategoryDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteCategory(1L));
        verify(categoryRepository).findById(1L);
        verify(categoryRepository, never()).delete(any());
    }
}
