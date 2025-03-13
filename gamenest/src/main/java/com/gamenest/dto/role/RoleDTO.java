package com.gamenest.dto.role;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RoleDTO {

    private Long id;

    @NotNull(message = "Role name cannot be null")
    @Size(min = 3, max = 100, message = "Role name must be between 3 and 100 characters")
    private String name;

}
