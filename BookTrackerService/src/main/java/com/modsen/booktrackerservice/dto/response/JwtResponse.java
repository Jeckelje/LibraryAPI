package com.modsen.booktrackerservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT response DTO")
public record JwtResponse(

        @Schema(description = "Unique identifier for the user.", example = "1")
        Long id,

        @Schema(description = "Username of the user.", example = "john_doe")
        String username,

        @Schema(description = "Access token used for authorization in the system.",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNjg3NzYzNjQ1fQ.G0OghgKt5EXXjKa0YktRuq3L6RmSgfhCzLXBj0vmeHg")
        String accessToken,

        @Schema(description = "Refresh token used to obtain a new access token after expiration.",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNjg3NzYzNjQ1fQ.YOpZjM3Gl0YozwF35IH0WLa4n0Qh49_v06PiEosot28")
        String refreshToken

) {
}

