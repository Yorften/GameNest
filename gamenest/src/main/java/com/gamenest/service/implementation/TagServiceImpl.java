package com.gamenest.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamenest.dto.tag.TagRequest;
import com.gamenest.dto.tag.UpdateTagRequest;
import com.gamenest.exception.DuplicateResourceException;
import com.gamenest.exception.ResourceNotFoundException;
import com.gamenest.mapper.TagMapper;
import com.gamenest.model.Tag;
import com.gamenest.repository.TagRepository;
import com.gamenest.service.interfaces.TagService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public TagRequest createTag(TagRequest tagRequest) {
        // Check for duplicate tag name
        tagRepository.findByName(tagRequest.getName())
                .ifPresent(existingTag -> {
                    throw new DuplicateResourceException("Tag with name " + tagRequest.getName() + " already exists");
                });
        Tag tag = tagMapper.convertToEntity(tagRequest);
        tag = tagRepository.save(tag);
        return tagMapper.convertToDTO(tag);
    }

    @Override
    public TagRequest updateTag(Long tagId, UpdateTagRequest updateTagRequest) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        tagMapper.updateEntity(tag, updateTagRequest);
        tag = tagRepository.save(tag);
        return tagMapper.convertToDTO(tag);
    }

    @Override
    public TagRequest getTagById(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        return tagMapper.convertToDTO(tag);
    }

    @Override
    public List<TagRequest> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tagMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        tagRepository.delete(tag);
    }
}
