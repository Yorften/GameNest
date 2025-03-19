package com.gamenest.dto.tag;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TagRequest {

    private Long id;

    @NotBlank(message = "Tag name is required")
    @Size(max = 255, message = "Tag name must be at most 255 characters")
    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
