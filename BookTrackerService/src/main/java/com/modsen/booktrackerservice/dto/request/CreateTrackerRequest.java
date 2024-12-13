package com.modsen.booktrackerservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

@Schema(description = "Create tracker DTO")
public record CreateTrackerRequest(

        @Schema(description = "Book ID", example = "4")
        @NotNull(message = "{tracker.book_id.notnull}")
        Long bookId,

        @Schema(description = "Book status", example = "free")
        @Pattern(regexp = "^(free|taken)$", message = "{tracker.status.invalid}")
        String status,


        @Schema(description = "Book taken date", example = "2024-01-10", format = "date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Nullable
        LocalDate takeDate,

        @Schema(description = "Book returned date", example = "2024-10-14", format = "date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Nullable
        LocalDate returnDate,

        @Schema(description = "Is deleted", example = "false")
        Boolean isDeleted
) {
}
