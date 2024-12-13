package com.modsen.booktrackerservice.mapper;

import com.modsen.booktrackerservice.dto.request.CreateUserRequest;
import com.modsen.booktrackerservice.dto.request.UpdateUserRequest;
import com.modsen.booktrackerservice.dto.response.UserResponse;
import com.modsen.booktrackerservice.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);

    User toUser(UserResponse userResponse);

    User toUser(CreateUserRequest createUserRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User toUser(UpdateUserRequest updateUserRequest);

    @Mapping(target = "id", ignore = true)
    void updateUserFromRequest(UpdateUserRequest updateUserRequest, @MappingTarget User user);

}
