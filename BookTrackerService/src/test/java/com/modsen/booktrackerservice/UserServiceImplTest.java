package com.modsen.booktrackerservice;

import com.modsen.booktrackerservice.dto.request.CreateUserRequest;
import com.modsen.booktrackerservice.dto.request.UpdateUserRequest;
import com.modsen.booktrackerservice.dto.response.UserResponse;
import com.modsen.booktrackerservice.exception.DuplicateResourceException;
import com.modsen.booktrackerservice.exception.ResourceNotFoundException;
import com.modsen.booktrackerservice.mapper.UserMapper;
import com.modsen.booktrackerservice.model.Role;
import com.modsen.booktrackerservice.model.User;
import com.modsen.booktrackerservice.repository.UserRepository;
import com.modsen.booktrackerservice.service.Impl.UserServiceImpl;
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

        UserResponse result = userService.createUser(request);

        assertEquals(response, result);
        verify(userRepository).existsUserByUsername(request.username());
        verify(userRepository).save(user);
    }

    @Test
    void createUser_duplicateUsername() {
        CreateUserRequest request = new CreateUserRequest("testuser", "password", "password");
        when(userRepository.existsUserByUsername(request.username())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> userService.createUser(request));
        verify(userRepository).existsUserByUsername(request.username());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_success() {
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

        UserResponse result = userService.updateUser(userId, request);

        assertEquals(response, result);
        verify(userRepository).findById(userId);
        verify(userRepository).save(user);
    }

    @Test
    void disableUser_success() {
        Long userId = 1L;
        User user = new User();
        user.setEnabled(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.disableUser(userId);

        assertFalse(user.isEnabled());
        verify(userRepository).save(user);
    }

    @Test
    void getUserById_success() {
        Long userId = 1L;
        User user = new User();
        UserResponse response = new UserResponse(userId, "testuser", "password", Set.of(Role.ROLE_USER), true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponse(user)).thenReturn(response);

        UserResponse result = userService.getUserById(userId);

        assertEquals(response, result);
        verify(userRepository).findById(userId);
    }

    @Test
    void getUserById_notFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
        verify(userRepository).findById(userId);
    }

    @Test
    void getAllUsers_success() {
        User user = new User();
        UserResponse response = new UserResponse(1L, "testuser", "password", Set.of(Role.ROLE_USER), true);

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toUserResponse(user)).thenReturn(response);

        List<UserResponse> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(response, result.get(0));
        verify(userRepository).findAll();
    }

}
