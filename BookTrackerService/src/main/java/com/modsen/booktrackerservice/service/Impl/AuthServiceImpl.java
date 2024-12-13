package com.modsen.booktrackerservice.service.Impl;

import com.modsen.booktrackerservice.dto.request.CreateJwtRequest;
import com.modsen.booktrackerservice.dto.response.JwtResponse;
import com.modsen.booktrackerservice.mapper.UserMapper;
import com.modsen.booktrackerservice.model.User;
import com.modsen.booktrackerservice.security.JwtTokenProvider;
import com.modsen.booktrackerservice.service.AuthService;
import com.modsen.booktrackerservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public JwtResponse login(CreateJwtRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(),
                        loginRequest.password()));
        User user = userMapper.toUser(userService.getUserByUsername(loginRequest.username()));
        return new JwtResponse(user.getId(), user.getUsername(),
                jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles()),
                jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername()));
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }
}
