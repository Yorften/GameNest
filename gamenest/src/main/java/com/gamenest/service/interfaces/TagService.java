package com.gamenest.service.interfaces;

import com.gamenest.dto.tag.TagRequest;
import com.gamenest.dto.tag.UpdateTagRequest;
import java.util.List;

public interface TagService {

    TagRequest createTag(TagRequest tagRequest);

    TagRequest updateTag(Long tagId, UpdateTagRequest updateTagRequest);

    TagRequest getTagById(Long tagId);

    List<TagRequest> getAllTags();

    void deleteTag(Long tagId);
}
