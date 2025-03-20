package com.gamenest.mapper;

import org.springframework.stereotype.Component;
import com.gamenest.dto.tag.TagRequest;
import com.gamenest.model.Tag;

@Component
public class TagMapper {

    public Tag convertToEntity(TagRequest tagRequest) {
        if (tagRequest == null) {
            return null;
        }
        return Tag.builder()
                .name(tagRequest.getName())
                .build();
    }

    public TagRequest convertToDTO(Tag tag) {
        if (tag == null) {
            return null;
        }
        return TagRequest.builder()
                .id(tag.getId())
                .name(tag.getName())
                .createdAt(tag.getCreatedAt())
                .updatedAt(tag.getUpdatedAt())
                .build();
    }
}
