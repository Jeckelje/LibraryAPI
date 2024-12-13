package com.modsen.booktrackerservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Tracker response DTO")
public record TrackerResponse(

        @Schema(description = "ID", example = "1")
        Long id,

        @Schema(description = "Book ID", example = "4")
        Long bookId,

        @Schema(description = "Book status", example = "free")
        String status,

        @Schema(description = "Book taken date", example = "2024-01-10", format = "date")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "{tracker.take_date.invalid}")
        @Nullable
        String takeDate,

        @Schema(description = "Book returned date", example = "2024-10-14", format = "date")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "{tracker.return_date.invalid}")
        @Nullable
        String returnDate

) {
}
