package com.gamenest.service.implementation;


import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gamenest.dto.user.UpdateUserDTO;
import com.gamenest.dto.user.UserDTO;
import com.gamenest.exception.ResourceNotFoundException;
import com.gamenest.mapper.UserMapper;
import com.gamenest.model.Role;
import com.gamenest.model.User;
import com.gamenest.repository.RoleRepository;
import com.gamenest.repository.UserRepository;
import com.gamenest.service.interfaces.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service implementation for User entity.
 * Defines methods for CRUD operations and additional business logic.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDTO getUserById(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        return userMapper.convertToDTO(user);
    }

    @Override
    public UserDTO getByUserName(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        return userMapper.convertToDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.convertToDTOList(users);
    }

    @Override
    public UserDTO addUser(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = userMapper.convertToEntity(userDTO);
        return userMapper.convertToDTO(userRepository.save(user));
    }

    @Override
    public UserDTO updateUser(Long userId, UpdateUserDTO userDTO) {
        User userDB = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        if (userDTO.getRole() != null) {
            Role role = roleRepository.findById(userDTO.getRole().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
            userDB.setRole(role);
        }

        return userMapper.convertToDTO(userRepository.save(userDB));
    }

    @Override
    public void deleteUserById(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

}
