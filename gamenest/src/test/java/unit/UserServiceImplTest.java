package unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.gamenest.dto.role.RoleDTO;
import com.gamenest.dto.user.UserRequest;
import com.gamenest.exception.ResourceNotFoundException;
import com.gamenest.mapper.UserMapper;
import com.gamenest.model.User;
import com.gamenest.repository.RoleRepository;
import com.gamenest.repository.UserRepository;
import com.gamenest.service.implementation.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    private UserServiceImpl userService;

    private User testUser;
    private UserRequest testUserDTO;
    private RoleDTO testRole;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(passwordEncoder, userRepository, userMapper);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testUserDTO = new UserRequest();
        testUserDTO.setId(1L);
        testUserDTO.setUsername("testuser");
        testUserDTO.setPassword("password123");

        testRole = new RoleDTO();
        testRole.setId(1L);
        testRole.setName("USER");

    }

    @Test
    void getUserById_WhenUserExists_ReturnsUserDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userMapper.convertToDTO(testUser)).thenReturn(testUserDTO);

        UserRequest result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(testUserDTO.getId(), result.getId());
        assertEquals(testUserDTO.getUsername(), result.getUsername());
        verify(userRepository).findById(1L);
        verify(userMapper).convertToDTO(testUser);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ThrowsResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository).findById(1L);
    }

    @Test
    void getByUserName_WhenUserExists_ReturnsUserDTO() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userMapper.convertToDTO(testUser)).thenReturn(testUserDTO);

        UserRequest result = userService.getByUserName("testuser");

        assertNotNull(result);
        assertEquals(testUserDTO.getUsername(), result.getUsername());
        verify(userRepository).findByUsername("testuser");
        verify(userMapper).convertToDTO(testUser);
    }

    @Test
    void getByUserName_WhenUserDoesNotExist_ThrowsResourceNotFoundException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getByUserName("testuser"));
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void getAllUsers_ReturnsPageOfUserDTOs() {
        List<User> users = (List.of(testUser));

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.convertToDTOList(List.of(testUser))).thenReturn(List.of(testUserDTO));

        List<UserRequest> result = userService.getAllUsers();

        assertNotNull(result);
        verify(userRepository).findAll();
    }

    @Test
    void addUser_SavesAndReturnsUserDTO() {
        String encodedPassword = "encodedPassword";
        String plainPassword = testUserDTO.getPassword();

        when(passwordEncoder.encode(testUserDTO.getPassword())).thenReturn(encodedPassword);
        when(userMapper.convertToEntity(testUserDTO)).thenReturn(testUser);
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(userMapper.convertToDTO(testUser)).thenReturn(testUserDTO);

        UserRequest result = userService.addUser(testUserDTO);

        assertNotNull(result);
        verify(passwordEncoder).encode(plainPassword);
        verify(userMapper).convertToEntity(testUserDTO);
        verify(userRepository).save(testUser);
        verify(userMapper).convertToDTO(testUser);
    }

    @Test
    void deleteUserById_WhenUserExists_DeletesUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        userService.deleteUserById(1L);

        verify(userRepository).findById(1L);
        verify(userRepository).delete(testUser);
    }

    @Test
    void deleteUserById_WhenUserDoesNotExist_ThrowsResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUserById(1L));
        verify(userRepository).findById(1L);
        verify(userRepository, never()).delete(any());
    }
}