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

import com.gamenest.dto.tag.TagRequest;
import com.gamenest.dto.tag.UpdateTagRequest;
import com.gamenest.exception.DuplicateResourceException;
import com.gamenest.exception.ResourceNotFoundException;
import com.gamenest.mapper.TagMapper;
import com.gamenest.model.Tag;
import com.gamenest.repository.TagRepository;
import com.gamenest.service.implementation.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagMapper tagMapper;

    private TagServiceImpl tagService;

    private Tag testTag;
    private TagRequest testTagRequest;
    private UpdateTagRequest testUpdateTagRequest;

    @BeforeEach
    void setUp() {
        tagService = new TagServiceImpl(tagRepository, tagMapper);

        testTag = Tag.builder()
                .id(1L)
                .name("Multiplayer")
                .build();

        testTagRequest = TagRequest.builder()
                .id(1L)
                .name("Multiplayer")
                .build();

        testUpdateTagRequest = UpdateTagRequest.builder()
                .name("Co-op")
                .build();
    }

    @Test
    void createTag_WhenTagDoesNotExist_ShouldCreateAndReturnDTO() {
        when(tagRepository.findByName(testTagRequest.getName())).thenReturn(Optional.empty());
        when(tagMapper.convertToEntity(testTagRequest)).thenReturn(testTag);
        when(tagRepository.save(testTag)).thenReturn(testTag);
        when(tagMapper.convertToDTO(testTag)).thenReturn(testTagRequest);

        TagRequest result = tagService.createTag(testTagRequest);

        assertNotNull(result);
        assertEquals(testTagRequest.getName(), result.getName());
        verify(tagRepository).findByName(testTagRequest.getName());
        verify(tagRepository).save(testTag);
    }

    @Test
    void createTag_WhenTagExists_ShouldThrowDuplicateResourceException() {
        when(tagRepository.findByName(testTagRequest.getName())).thenReturn(Optional.of(testTag));

        assertThrows(DuplicateResourceException.class, () -> tagService.createTag(testTagRequest));
        verify(tagRepository).findByName(testTagRequest.getName());
        verify(tagRepository, never()).save(any());
    }

    @Test
    void updateTag_WhenTagExists_ShouldUpdateAndReturnDTO() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(testTag));
        // Simulate updating the entity using the mapper
        // (Alternatively, you might check that testTag's name is updated)
        when(tagRepository.save(testTag)).thenReturn(testTag);
        when(tagMapper.convertToDTO(testTag)).thenReturn(testTagRequest);

        TagRequest result = tagService.updateTag(1L, testUpdateTagRequest);

        assertNotNull(result);
        verify(tagRepository).findById(1L);
        verify(tagRepository).save(testTag);
    }

    @Test
    void updateTag_WhenTagDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tagService.updateTag(1L, testUpdateTagRequest));
        verify(tagRepository).findById(1L);
        verify(tagRepository, never()).save(any());
    }

    @Test
    void getTagById_WhenTagExists_ShouldReturnDTO() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(testTag));
        when(tagMapper.convertToDTO(testTag)).thenReturn(testTagRequest);

        TagRequest result = tagService.getTagById(1L);

        assertNotNull(result);
        verify(tagRepository).findById(1L);
    }

    @Test
    void getTagById_WhenTagDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tagService.getTagById(1L));
        verify(tagRepository).findById(1L);
    }

    @Test
    void getAllTags_ShouldReturnListOfDTOs() {
        List<Tag> tagList = List.of(testTag);
        when(tagRepository.findAll()).thenReturn(tagList);
        when(tagMapper.convertToDTO(testTag)).thenReturn(testTagRequest);

        List<TagRequest> result = tagService.getAllTags();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tagRepository).findAll();
    }

    @Test
    void deleteTag_WhenTagExists_ShouldDeleteTag() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(testTag));

        tagService.deleteTag(1L);

        verify(tagRepository).findById(1L);
        verify(tagRepository).delete(testTag);
    }

    @Test
    void deleteTag_WhenTagDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tagService.deleteTag(1L));
        verify(tagRepository).findById(1L);
        verify(tagRepository, never()).delete(any());
    }
}
