package com.modsen.booktrackerservice.swagger;

import com.modsen.booktrackerservice.dto.error.AppError;
import com.modsen.booktrackerservice.dto.request.UpdateStatusRequest;
import com.modsen.booktrackerservice.dto.response.TrackerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Tracker Controller", description = "Tracker API")
public interface TrackerAPI {

    @Operation(summary = "Create tracker")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tracker created successfully", content = @Content(schema = @Schema(implementation = TrackerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = AppError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = AppError.class)))
    })
    void createTracker(@PathVariable Long bookId);

    @Operation(summary = "Get all trackers where status is free")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trackers retrieved successfully", content = @Content(schema = @Schema(implementation = TrackerResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = AppError.class)))
    })
    List<TrackerResponse> getAllTrackersWhereStatusIsFree();

    @Operation(summary = "Update tracker status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tracker status updated successfully", content = @Content(schema = @Schema(implementation = TrackerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = AppError.class))),
            @ApiResponse(responseCode = "404", description = "Tracker not found", content = @Content(schema = @Schema(implementation = AppError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = AppError.class)))
    })
    TrackerResponse changeStatus(@PathVariable Long bookId, @RequestBody @Valid UpdateStatusRequest updateStatusRequest);

    @Operation(summary = "Delete tracker by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tracker deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Tracker not found", content = @Content(schema = @Schema(implementation = AppError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = AppError.class)))
    })
    void softDeleteTrackerById(@PathVariable Long id);

    @Operation(summary = "Delete tracker by book ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tracker deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Tracker not found", content = @Content(schema = @Schema(implementation = AppError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = AppError.class)))
    })
    void softDeleteTrackerByBookId(@PathVariable Long bookId);

}
