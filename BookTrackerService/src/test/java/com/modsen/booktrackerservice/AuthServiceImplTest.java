package com.modsen.booktrackerservice;

import com.modsen.booktrackerservice.dto.request.CreateJwtRequest;
import com.modsen.booktrackerservice.dto.response.JwtResponse;
import com.modsen.booktrackerservice.dto.response.UserResponse;
import com.modsen.booktrackerservice.mapper.UserMapper;
import com.modsen.booktrackerservice.model.Role;
import com.modsen.booktrackerservice.model.User;
import com.modsen.booktrackerservice.security.JwtTokenProvider;
import com.modsen.booktrackerservice.service.Impl.AuthServiceImpl;
import com.modsen.booktrackerservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

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

        JwtResponse result = authService.login(loginRequest);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).getUserByUsername(loginRequest.username());
        verify(jwtTokenProvider).createAccessToken(user.getId(), user.getUsername(), user.getRoles());
        verify(jwtTokenProvider).createRefreshToken(user.getId(), user.getUsername());
    }

    @Test
    void refresh_success() {
        String refreshToken = "validRefreshToken";
        JwtResponse expectedResponse = new JwtResponse(1L, "testuser", "newAccessToken", "newRefreshToken");

        when(jwtTokenProvider.refreshUserTokens(refreshToken)).thenReturn(expectedResponse);

        JwtResponse result = authService.refresh(refreshToken);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(jwtTokenProvider).refreshUserTokens(refreshToken);
    }
}
