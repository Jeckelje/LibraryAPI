package com.modsen.booktrackerservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdateStatusRequest(

        @Schema(description = "Book status", example = "free")
        @Pattern(regexp = "^(free|taken)$", message = "{tracker.status.invalid}")
        @NotNull(message = "{tracker.status.notnull}")
        String status

) {
}
