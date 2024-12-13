package com.modsen.bookstorageservice;

import com.modsen.bookstorageservice.dto.request.CreateJwtRequest;
import com.modsen.bookstorageservice.dto.response.JwtResponse;
import com.modsen.bookstorageservice.dto.response.UserResponse;
import com.modsen.bookstorageservice.mapper.UserMapper;
import com.modsen.bookstorageservice.model.Role;
import com.modsen.bookstorageservice.model.User;
import com.modsen.bookstorageservice.security.JwtTokenProvider;
import com.modsen.bookstorageservice.service.UserService;
import com.modsen.bookstorageservice.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_success() {
        // Arrange
        CreateJwtRequest loginRequest = new CreateJwtRequest("testuser", "password");
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRoles(Set.of(Role.ROLE_USER));

        JwtResponse expectedResponse = new JwtResponse(1L, "testuser", "accessToken", "refreshToken");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null); // Spring Security does not return a meaningful object here.
        when(userService.getUserByUsername(loginRequest.username()))
                .thenReturn(new UserResponse(1L, "testuser", "password", Set.of(Role.ROLE_USER), true));
        when(userMapper.toUser(any(UserResponse.class))).thenReturn(user);
        when(jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles()))
                .thenReturn("accessToken");
        when(jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername()))
                .thenReturn("refreshToken");

        // Act
        JwtResponse result = authService.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).getUserByUsername(loginRequest.username());
        verify(jwtTokenProvider).createAccessToken(user.getId(), user.getUsername(), user.getRoles());
        verify(jwtTokenProvider).createRefreshToken(user.getId(), user.getUsername());
    }

    @Test
    void refresh_success() {
        // Arrange
        String refreshToken = "validRefreshToken";
        JwtResponse expectedResponse = new JwtResponse(1L, "testuser", "newAccessToken", "newRefreshToken");

        when(jwtTokenProvider.refreshUserTokens(refreshToken)).thenReturn(expectedResponse);

        // Act
        JwtResponse result = authService.refresh(refreshToken);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(jwtTokenProvider).refreshUserTokens(refreshToken);
    }
}
