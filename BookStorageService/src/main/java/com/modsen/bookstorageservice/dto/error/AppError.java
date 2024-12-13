package com.modsen.bookstorageservice.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "Error response model")
public record AppError
        (
                @Schema(description = "HTTP status code", example = "400")
                int status,

                @Schema(description = "Error message", example = "Bad Request")
                String message,

                @Schema(description = "Timestamp of the error", example = "2024-06-28T08:26:53.025Z")
                LocalDateTime timestamp

        ) {
}