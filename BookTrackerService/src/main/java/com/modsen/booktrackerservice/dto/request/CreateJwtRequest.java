package com.modsen.booktrackerservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request DTO for creating JWT tokens")
public record CreateJwtRequest(

        @Schema(description = "Username of the user attempting to authenticate.", example = "john_doe")
        @NotNull(message = "{user.username.notnull}")
        @Size(min = 2, max = 64, message = "{user.username.size}")
        String username,

        @Schema(description = "Password of the user attempting to authenticate.", example = "password123")
        @NotNull(message = "{user.password.notnull}")
        @Size(min = 2, max = 256, message = "{user.password.size}")
        String password

) {
}

