package com.gamenest.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gamenest.config.jwt.JWTGenerator;
import com.gamenest.dto.auth.AuthResponse;
import com.gamenest.dto.auth.LoginRequest;
import com.gamenest.dto.user.UserRequest;
import com.gamenest.model.TokenBlacklist;
import com.gamenest.repository.TokenBlacklistRepository;
import com.gamenest.service.interfaces.UserService;
import com.gamenest.validation.UserValidationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing authentication routes.
 * Handles HTTP requests and routes them to the appropriate service methods.
 */
@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "APIs for user authentication and account management")
public class AuthController {
    private final UserValidationService userValidationService;
    private final UserService userService;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTGenerator tokenGenerator;
    private final HttpServletRequest request;

    @Operation(summary = "Login user", description = "Authenticates a user and generates a JWT token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Parameter(description = "Login credentials: username and password", required = true) @RequestBody @Valid LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword());

        WebAuthenticationDetails details = new WebAuthenticationDetails(request);
        authenticationToken.setDetails(details);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String username = authentication.getName();
        UserRequest user = userService.getByUserName(username);

        String token = tokenGenerator.generateToken(authentication);

        return new ResponseEntity<>(new AuthResponse(token, user), HttpStatus.OK);
    }

    @Operation(summary = "Register user", description = "Registers a new user with the provided details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid user data or validation error")
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Parameter(description = "User registration details: username, email, password, and role", required = true) @RequestBody @Valid UserRequest userDTO) {

        if (userValidationService.isUsernameTaken(userDTO.getUsername())) {
            log.info("Username is already taken");
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        if (userValidationService.isEmailTaken(userDTO.getEmail())) {
            log.info("Email is already taken");
            return ResponseEntity.badRequest().body("Email is already taken");
        }

        if (!userValidationService.doPasswordsMatch(userDTO.getPassword(), userDTO.getRepeatPassword())) {
            log.info("Passwords do not match");
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        userService.addUser(userDTO);

        return ResponseEntity.ok("User registered successfully");
    }

    @Operation(summary = "Logout user", description = "Invalidates the JWT token to log out the user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "400", description = "Invalid token")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @Parameter(description = "Authorization token to be invalidated", required = true) @RequestHeader("Authorization") String token) {

        String jwtToken = token.substring(7);

        tokenBlacklistRepository.save(TokenBlacklist.builder().token(jwtToken).build());

        return new ResponseEntity<>("Logout successful", HttpStatus.OK);

    }
}
