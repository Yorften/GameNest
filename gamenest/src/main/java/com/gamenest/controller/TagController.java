package com.gamenest.controller;

import jakarta.validation.Valid;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gamenest.dto.tag.TagRequest;
import com.gamenest.dto.tag.UpdateTagRequest;
import com.gamenest.service.interfaces.TagService;


@RestController
@RequestMapping("/api/v1/tags")
@Tag(name = "Tag API", description = "Endpoints for managing tags associated with games")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @Operation(summary = "Create a new tag", description = "Creates a new tag. Tag name must be unique.")
    @PostMapping
    public ResponseEntity<TagRequest> createTag(@Valid @RequestBody TagRequest tagRequest) {
        TagRequest createdTag = tagService.createTag(tagRequest);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a tag", description = "Updates an existing tag with new details.")
    @PutMapping("/{tagId}")
    public ResponseEntity<TagRequest> updateTag(
            @PathVariable Long tagId,
            @RequestBody UpdateTagRequest updateTagRequest) {
        TagRequest updatedTag = tagService.updateTag(tagId, updateTagRequest);
        return ResponseEntity.ok(updatedTag);
    }

    @Operation(summary = "Get a tag by ID", description = "Retrieves a tag by its ID.")
    @GetMapping("/{tagId}")
    public ResponseEntity<TagRequest> getTagById(@PathVariable Long tagId) {
        TagRequest tag = tagService.getTagById(tagId);
        return ResponseEntity.ok(tag);
    }

    @Operation(summary = "Get all tags", description = "Retrieves a list of all tags.")
    @GetMapping
    public ResponseEntity<List<TagRequest>> getAllTags() {
        List<TagRequest> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    @Operation(summary = "Delete a tag", description = "Deletes a tag by its ID.")
    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.noContent().build();
    }
}
