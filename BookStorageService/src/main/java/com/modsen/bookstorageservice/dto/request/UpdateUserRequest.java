package com.modsen.bookstorageservice.dto.request;

import com.modsen.bookstorageservice.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Schema(description = "Update user DTO")
public record UpdateUserRequest(

        @Schema(description = "User's username, must be unique.", example = "john_doe")
        @NotNull(message = "{user.username.notnull}")
        @Size(min = 2, max = 64, message = "{user.username.size}")
        String username,

        @Schema(description = "User's password, stored in hashed format.", example = "password123")
        @NotNull(message = "{user.password.notnull}")
        @Size(min = 2, max = 256, message = "{user.password.size}")
        String password,

        @Schema(description = "Role assigned to the user. Available roles: ROLE_USER, ROLE_ADMIN.", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
        @NotNull(message = "{user.roles.notnull}")
        @Size(min = 1, message = "{user.roles.size}")
        Set<Role> roles,

        @Schema(description = "Indicates whether the user's account is enabled.", example = "true")
        @NotNull(message = "{user.enabled.notnull}")
        boolean enabled

) {
}
