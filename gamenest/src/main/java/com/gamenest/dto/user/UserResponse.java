package com.gamenest.dto.user;

import com.gamenest.dto.role.RoleDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private String email;

    private RoleDTO role;

}
