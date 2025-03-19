package com.gamenest.dto.tag;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UpdateTagRequest {

    @Size(max = 255, message = "Tag name must be at most 255 characters")
    private String name;

}
