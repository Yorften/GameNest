package com.gamenest.dto.game;

import com.gamenest.dto.role.RoleDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GameResponse {

    private Long id;

    private String username;

    private String email;

    private RoleDTO role;

}
