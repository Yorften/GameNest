package com.gamenest.service.interfaces;

import java.util.List;

import com.gamenest.dto.user.UpdateUserDTO;
import com.gamenest.dto.user.UserDTO;

/**
 * Service interface for User entity.
 * Defines methods for CRUD operations and additional business logic.
 */
public interface UserService {

    UserDTO getUserById(Long id);

    public UserDTO getByUserName(String userName);

    List<UserDTO> getAllUsers();

    UserDTO addUser(UserDTO User);

    public UserDTO updateUser(Long UserId, UpdateUserDTO User);

    public void deleteUserById(Long UserId);

}
