package com.modsen.bookstorageservice.mapper;

import com.modsen.bookstorageservice.dto.request.CreateUserRequest;
import com.modsen.bookstorageservice.dto.request.UpdateBookRequest;
import com.modsen.bookstorageservice.dto.request.UpdateUserRequest;
import com.modsen.bookstorageservice.dto.response.UserResponse;
import com.modsen.bookstorageservice.model.Book;
import com.modsen.bookstorageservice.model.User;
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
