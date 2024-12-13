package com.modsen.booktrackerservice.dto.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "Error response model")
public record AppError(
        @Schema(description = "HTTP status code", example = "200")
        int status,

        @Schema(description = "Error message", example = "OK")
        String message,

        @Schema(description = "Timestamp of the error", example = "2024-06-28T15:30:00", format = "date-time")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp
) {
}
