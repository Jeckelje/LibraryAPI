package com.modsen.bookstorageservice.service.impl;

import com.modsen.bookstorageservice.dto.request.CreateJwtRequest;
import com.modsen.bookstorageservice.dto.response.JwtResponse;
import com.modsen.bookstorageservice.mapper.UserMapper;
import com.modsen.bookstorageservice.model.User;
import com.modsen.bookstorageservice.security.JwtTokenProvider;
import com.modsen.bookstorageservice.service.AuthService;
import com.modsen.bookstorageservice.service.UserService;
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
