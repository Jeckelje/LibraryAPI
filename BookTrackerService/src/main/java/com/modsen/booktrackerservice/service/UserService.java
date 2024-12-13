package com.modsen.booktrackerservice.service;

import com.modsen.booktrackerservice.dto.request.CreateUserRequest;
import com.modsen.booktrackerservice.dto.request.UpdateUserRequest;
import com.modsen.booktrackerservice.dto.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserResponse createUser(CreateUserRequest createUserRequest);

    UserResponse updateUser(Long id, UpdateUserRequest updateUserRequest);

    void disableUser(Long id);

    UserResponse getUserById(Long id);

    UserResponse getUserByUsername(String username);

    List<UserResponse> getAllUsers();
}
