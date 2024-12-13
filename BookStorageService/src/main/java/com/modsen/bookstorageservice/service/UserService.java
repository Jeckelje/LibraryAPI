package com.modsen.bookstorageservice.service;

import com.modsen.bookstorageservice.dto.request.CreateUserRequest;
import com.modsen.bookstorageservice.dto.request.UpdateUserRequest;
import com.modsen.bookstorageservice.dto.response.UserResponse;
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
