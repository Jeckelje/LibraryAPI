package com.modsen.bookstorageservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Create user DTO")
public record CreateUserRequest(

        @Schema(description = "User's username, must be unique.", example = "john_doe")
        @NotNull(message = "{user.username.notnull}")
        @Size(min = 2, max = 64, message = "{user.username.size}")
        String username,

        @Schema(description = "User's password, stored in hashed format.", example = "password123")
        @NotNull(message = "{user.password.notnull}")
        @Size(min = 2, max = 256, message = "{user.password.size}")
        String password,

        @Schema(description = "Field for password confirmation during registration. Not stored in the database.", example = "password123")
        @NotNull(message = "{user.password.notnull}")
        @Size(min = 2, max = 256, message = "{user.password.size}")
        String passwordConfirm

) {
}
