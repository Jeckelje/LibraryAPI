package com.modsen.booktrackerservice.controller;

import com.modsen.booktrackerservice.dto.request.UpdateStatusRequest;
import com.modsen.booktrackerservice.dto.response.TrackerResponse;
import com.modsen.booktrackerservice.service.TrackerService;
import com.modsen.booktrackerservice.swagger.TrackerAPI;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/tracker")
@Validated
public class TrackerController implements TrackerAPI {

    private final TrackerService trackerService;

    @PostMapping("/create-tracker/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public TrackerResponse createTracker(@PathVariable Long bookId) {
        return trackerService.createTracker(bookId);
    }

    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public List<TrackerResponse> getAllTrackersWhereStatusIsFree() {
        return trackerService.getAllTrackersWhereStatusIsFree();
    }

    @PutMapping("/update-status/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public TrackerResponse changeStatus(
            @PathVariable Long bookId,
            @RequestBody @Valid UpdateStatusRequest updateStatusRequest) {
        return trackerService.changeStatus(bookId, updateStatusRequest);
    }

    @DeleteMapping("/delete-by-id/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public void softDeleteTrackerById(@PathVariable Long id) {
        trackerService.softDeleteTrackerById(id);
    }

    @DeleteMapping("/delete-by-book-id/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public void softDeleteTrackerByBookId(@PathVariable Long bookId) {
        trackerService.softDeleteTrackerByBookId(bookId);
    }

}
