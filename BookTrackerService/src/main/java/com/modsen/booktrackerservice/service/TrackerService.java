package com.modsen.booktrackerservice.service;

import com.modsen.booktrackerservice.dto.request.UpdateStatusRequest;
import com.modsen.booktrackerservice.dto.response.TrackerResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TrackerService {

    TrackerResponse createTracker(Long bookId);

    List<TrackerResponse> getAllTrackersWhereStatusIsFree();

    TrackerResponse changeStatus(Long bookId, UpdateStatusRequest updateStatusRequest);

    void softDeleteTrackerById(Long id);

    void softDeleteTrackerByBookId(Long bookId);

}
