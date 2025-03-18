package com.gamenest.service.interfaces;

import java.util.List;

import com.gamenest.dto.user.UserRequest;

/**
 * Service interface for User entity.
 * Defines methods for CRUD operations and additional business logic.
 */
public interface UserService {

    UserRequest getUserById(Long id);

    public UserRequest getByUserName(String userName);

    List<UserRequest> getAllUsers();

    UserRequest addUser(UserRequest User);

    public void deleteUserById(Long UserId);

}
