package com.modsen.booktrackerservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.modsen.booktrackerservice.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "User response DTO")
public record UserResponse(

        @Schema(description = "Unique identifier for the user.", example = "1")
        Long id,

        @Schema(description = "User's username, must be unique.", example = "john_doe")
        String username,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password,

        @Schema(description = "Role assigned to the user. Available roles: ROLE_USER, ROLE_ADMIN.", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
        Set<Role> roles,

        @Schema(description = "Indicates whether the user's account is enabled.", example = "true")
        boolean enabled

) {
}

