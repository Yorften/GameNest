package com.gamenest.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gamenest.dto.role.RoleDTO;
import com.gamenest.dto.user.UserRequest;
import com.gamenest.exception.InvalidDataException;
import com.gamenest.model.Role;
import com.gamenest.model.User;
import com.gamenest.service.interfaces.RoleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserMapper {
    private final List<String> VALID_INCLUDES = Arrays.asList("role");

    private final RoleService roleService;

    public void verifyIncludes(String... with)
            throws InvalidDataException {
        List<String> includesList = Arrays.asList(with);

        for (String include : includesList) {
            if (!include.isEmpty() && !VALID_INCLUDES.contains(include)) {
                throw new InvalidDataException("Invalid include: " + include);
            }
        }
    }

    public User convertToEntity(UserRequest userDTO) {
        Role role = roleService.getRoleByName(userDTO.getRole().getName());

        return User.builder()
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .role(role)
                .build();
    }

    public UserRequest convertToDTO(User user) {
        Role role = roleService.getRoleByName(user.getRole().getName());

        return UserRequest.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .installationId(user.getInstallationId())
                .role(RoleDTO.builder().id(role.getId()).name(role.getName()).build())
                .build();
    }

    public List<UserRequest> convertToDTOList(List<User> users) {
        return users.stream()
                .map(user -> convertToDTO(user))
                .collect(Collectors.toList());
    }

    public UserRequest convertToDTO(User user, String... with) {
        List<String> includesList = Arrays.asList(with);

        RoleDTO roleDTO = null;

        if (includesList.contains("role")) {
            Role role = user.getRole();
            roleDTO = RoleDTO.builder()
                    .name(role.getName())
                    .build();
        }

        if (includesList.contains("employee")) {

        }

        return UserRequest.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .role(roleDTO)
                .build();
    }

    public List<UserRequest> convertToDTOList(List<User> users, String... with) {
        verifyIncludes(with);
        return users.stream()
                .map(user -> convertToDTO(user, with))
                .collect(Collectors.toList());
    }
}
