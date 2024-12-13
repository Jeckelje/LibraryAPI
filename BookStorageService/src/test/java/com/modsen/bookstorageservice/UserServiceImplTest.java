package com.modsen.bookstorageservice;

import com.modsen.bookstorageservice.dto.request.CreateUserRequest;
import com.modsen.bookstorageservice.dto.request.UpdateUserRequest;
import com.modsen.bookstorageservice.dto.response.UserResponse;
import com.modsen.bookstorageservice.exception.DuplicateResourceException;
import com.modsen.bookstorageservice.exception.ResourceNotFoundException;
import com.modsen.bookstorageservice.mapper.UserMapper;
import com.modsen.bookstorageservice.model.Role;
import com.modsen.bookstorageservice.model.User;
import com.modsen.bookstorageservice.repository.UserRepository;
import com.modsen.bookstorageservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void createUser_success() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("testuser", "password", "password");
        User user = new User();
        user.setUsername(request.username());
        user.setPassword("encodedPassword");
        user.setRoles(Set.of(Role.ROLE_USER));
        UserResponse response = new UserResponse(1L, "testuser", "password", Set.of(Role.ROLE_USER), true);

        when(userRepository.existsUserByUsername(request.username())).thenReturn(false);
        when(userMapper.toUser(request)).thenReturn(user);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserResponse(user)).thenReturn(response);

        // Act
        UserResponse result = userService.createUser(request);

        // Assert
        assertEquals(response, result);
        verify(userRepository).existsUserByUsername(request.username());
        verify(userRepository).save(user);
    }

    @Test
    void createUser_duplicateUsername() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("testuser", "password", "password");
        when(userRepository.existsUserByUsername(request.username())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> userService.createUser(request));
        verify(userRepository).existsUserByUsername(request.username());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_success() {
        // Arrange
        Long userId = 1L;
        UpdateUserRequest request = new UpdateUserRequest("updateduser", "password", Set.of(Role.ROLE_USER), true);
        User user = new User();
        User updatedUser = new User();
        UserResponse response = new UserResponse(userId, "updateduser", "password", Set.of(Role.ROLE_USER), true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doAnswer(invocation -> {
            User u = invocation.getArgument(1);
            u.setUsername(request.username());
            u.setEnabled(request.enabled());
            return null;
        }).when(userMapper).updateUserFromRequest(eq(request), eq(user));
        when(userRepository.save(user)).thenReturn(updatedUser);
        when(userMapper.toUserResponse(updatedUser)).thenReturn(response);

        // Act
        UserResponse result = userService.updateUser(userId, request);

        // Assert
        assertEquals(response, result);
        verify(userRepository).findById(userId);
        verify(userRepository).save(user);
    }

    @Test
    void disableUser_success() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setEnabled(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        userService.disableUser(userId);

        // Assert
        assertFalse(user.isEnabled());
        verify(userRepository).save(user);
    }

    @Test
    void getUserById_success() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        UserResponse response = new UserResponse(userId, "testuser", "password", Set.of(Role.ROLE_USER), true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponse(user)).thenReturn(response);

        // Act
        UserResponse result = userService.getUserById(userId);

        // Assert
        assertEquals(response, result);
        verify(userRepository).findById(userId);
    }

    @Test
    void getUserById_notFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
        verify(userRepository).findById(userId);
    }

    @Test
    void getAllUsers_success() {
        // Arrange
        User user = new User();
        UserResponse response = new UserResponse(1L, "testuser", "password", Set.of(Role.ROLE_USER), true);

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toUserResponse(user)).thenReturn(response);

        // Act
        List<UserResponse> result = userService.getAllUsers();

        // Assert
        assertEquals(1, result.size());
        assertEquals(response, result.get(0));
        verify(userRepository).findAll();
    }

}
