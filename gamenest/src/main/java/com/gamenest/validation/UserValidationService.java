package com.gamenest.validation;

import org.springframework.stereotype.Service;

import com.gamenest.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserValidationService {

    private final UserRepository userRepository;

    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean doPasswordsMatch(String password, String repeatPassword) {
        return password.trim().equals(repeatPassword.trim());
    }

}
