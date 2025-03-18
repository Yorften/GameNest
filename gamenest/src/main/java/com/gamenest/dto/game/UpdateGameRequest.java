package com.gamenest.dto.game;

import jakarta.validation.constraints.NotNull;

import com.gamenest.dto.role.RoleDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateGameRequest {

    @NotNull(message = "Role shouldn't be null")
    private RoleDTO role;

}
