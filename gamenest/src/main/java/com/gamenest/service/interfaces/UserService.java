package com.gamenest.service.interfaces;

import java.util.List;

import com.gamenest.dto.game.GameRequest;
import com.gamenest.dto.user.UserRequest;

/**
 * Service interface for User entity.
 * Defines methods for CRUD operations and additional business logic.
 */
public interface UserService {

    UserRequest getUserById(Long id);

    UserRequest getByUserName(String userName);

    List<UserRequest> getAllUsers();

    UserRequest addUser(UserRequest User);

    void deleteUserById(Long UserId);

    UserRequest updateUserInstallation(Long installationId, String username);

    List<GameRequest> getUserGames(String username);

}
