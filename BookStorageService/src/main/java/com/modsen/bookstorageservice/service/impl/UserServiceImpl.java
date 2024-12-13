package com.modsen.bookstorageservice.service.impl;

import com.modsen.bookstorageservice.dto.request.CreateUserRequest;
import com.modsen.bookstorageservice.dto.request.UpdateUserRequest;
import com.modsen.bookstorageservice.dto.response.UserResponse;
import com.modsen.bookstorageservice.error.ErrorMessages;
import com.modsen.bookstorageservice.exception.DuplicateResourceException;
import com.modsen.bookstorageservice.exception.ResourceNotFoundException;
import com.modsen.bookstorageservice.mapper.UserMapper;
import com.modsen.bookstorageservice.model.Role;
import com.modsen.bookstorageservice.model.User;
import com.modsen.bookstorageservice.repository.UserRepository;
import com.modsen.bookstorageservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        checkUserExistenceByUsernameAndThrow(createUserRequest.username());

        User user = userMapper.toUser(createUserRequest);
        user.setPassword(passwordEncoder.encode(createUserRequest.password()));
        user.setEnabled(true);
        user.setRoles(Set.of(Role.ROLE_USER));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest updateUserRequest) {
        User existingUser = findUserByIdOrThrow(id);

        userMapper.updateUserFromRequest(updateUserRequest, existingUser);
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public void disableUser(Long id) {
        User user = findUserByIdOrThrow(id);
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = findUserByIdOrThrow(id);
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        checkUserExistenceByUsernameOrThrow(username);
        //System.out.println("avbhlaebrhliebhiaebrhiaebuieauiebui");
        return userMapper.toUserResponse(userRepository.findUserByUsername(username));
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    //------------------------------------------------------
    private User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessages.RESOURCE_NOT_FOUND_BY_ID_MESSAGE, "User", id)));
    }

    private boolean checkUserExistenceByUsername(String username) {
        return userRepository.existsUserByUsername(username);
    }

    private void checkUserExistenceByUsernameAndThrow(String login) {
        if (checkUserExistenceByUsername(login)) {
            throw new DuplicateResourceException(
                    String.format(ErrorMessages.DUPLICATE_RESOURCE_MESSAGE, "User", "username"));
        }
    }

    private void checkUserExistenceByUsernameOrThrow(String username) {
        if (!checkUserExistenceByUsername(username)) {
            throw new ResourceNotFoundException(
                    String.format(ErrorMessages.RESOURCE_NOT_FOUND_BY_FIELD_MESSAGE, "User", "username"));
        }
    }
}
