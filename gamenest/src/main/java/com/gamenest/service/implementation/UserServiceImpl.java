package com.gamenest.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gamenest.dto.game.GameRequest;
import com.gamenest.dto.user.UserRequest;
import com.gamenest.exception.ResourceNotFoundException;
import com.gamenest.mapper.GameMapper;
import com.gamenest.mapper.UserMapper;
import com.gamenest.model.User;
import com.gamenest.repository.GameRepository;
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

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final UserMapper userMapper;
    private final GameMapper gameMapper;

    @Override
    public UserRequest getUserById(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.convertToDTO(user);
    }

    @Override
    public UserRequest getByUserName(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.convertToDTO(user);
    }

    @Override
    public List<UserRequest> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.convertToDTOList(users);
    }

    @Override
    public UserRequest addUser(UserRequest userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = userMapper.convertToEntity(userDTO);
        return userMapper.convertToDTO(userRepository.save(user));
    }

    @Override
    public void deleteUserById(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public UserRequest updateUserInstallation(Long installationId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setInstallationId(installationId);
        return userMapper.convertToDTO(userRepository.save(user));
    }

    @Override
    public List<GameRequest> getUserGames(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return gameRepository.findByOwnerId(user.getId()).stream()
                .map(gameMapper::convertToDTO)
                .collect(Collectors.toList());

    }
}
