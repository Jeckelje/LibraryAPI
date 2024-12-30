package com.modsen.bookstorageservice.controller;

import com.modsen.bookstorageservice.dto.request.CreateJwtRequest;
import com.modsen.bookstorageservice.dto.request.CreateUserRequest;
import com.modsen.bookstorageservice.dto.response.JwtResponse;
import com.modsen.bookstorageservice.dto.response.UserResponse;
import com.modsen.bookstorageservice.mapper.UserMapper;
import com.modsen.bookstorageservice.service.AuthService;
import com.modsen.bookstorageservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth-books")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody CreateJwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public UserResponse register(@Validated @RequestBody CreateUserRequest createUserRequest) {
        return userService.createUser(createUserRequest);

    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
