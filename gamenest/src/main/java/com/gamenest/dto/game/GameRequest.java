package com.gamenest.dto.game;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.gamenest.dto.role.RoleDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GameRequest {

    private Long id;

    @NotNull(message = "Gamename cannot be null")
    @Size(min = 4, max = 32, message = "Gamename must be between 6 and 32 characters")
    private String username;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters")
    private String password;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters")
    private String repeatPassword;

    @NotNull(message = "Email cannot be null")
    @Size(min = 8, max = 32, message = "Email must be between 8 and 32 characters")
    @Email(message = "Error in email format")
    private String email;

    @NotNull(message = "Role not provided")
    private RoleDTO role;

}
