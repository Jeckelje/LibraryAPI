package com.modsen.booktrackerservice.service.Impl;

import com.modsen.booktrackerservice.dto.request.UpdateStatusRequest;
import com.modsen.booktrackerservice.dto.response.TrackerResponse;
import com.modsen.booktrackerservice.error.ErrorMessages;
import com.modsen.booktrackerservice.exception.DuplicateResourceException;
import com.modsen.booktrackerservice.exception.ResourceNotFoundException;
import com.modsen.booktrackerservice.mapper.TrackerMapper;
import com.modsen.booktrackerservice.model.Tracker;
import com.modsen.booktrackerservice.repository.TrackerRepository;
import com.modsen.booktrackerservice.service.TrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackerServiceImpl implements TrackerService {

    private final TrackerRepository trackerRepository;
    private final TrackerMapper trackerMapper;

    @Override
    public TrackerResponse createTracker(Long bookId) {
        checkTrackerExistenceByBookIdAndThrow(bookId);

        Tracker tracker = new Tracker();
        tracker.setBookId(bookId);
        tracker.setStatus("free");
        tracker.setTakeDate(null);
        tracker.setReturnDate(null);
        tracker.setDeleted(false);
        return trackerMapper.toTrackerResponse(trackerRepository.save(tracker));
    }

    @Override
    public List<TrackerResponse> getAllTrackersWhereStatusIsFree() {
        return trackerRepository.getAllByStatus("free")
                .stream()
                .map(trackerMapper::toTrackerResponse)
                .toList();
    }

    @Override
    public TrackerResponse changeStatus(Long bookId, UpdateStatusRequest updateStatusRequest) {
        checkTrackerExistenceByBookIdOrThrow(bookId);
        Tracker tracker = trackerRepository.findByBookId(bookId);
        tracker.setStatus(updateStatusRequest.status());
        Tracker updatedTracker = trackerRepository.save(tracker);
        return trackerMapper.toTrackerResponse(updatedTracker);
    }

    @Override
    public void softDeleteTrackerById(Long id) {
        Tracker tracker = findTrackerByIdOrThrow(id);
        tracker.setDeleted(true);
        trackerRepository.save(tracker);
    }

    @Override
    public void softDeleteTrackerByBookId(Long bookId) {
        checkTrackerExistenceByBookIdOrThrow(bookId);
        Tracker tracker = trackerRepository.findByBookId(bookId);
        tracker.setDeleted(true);
        trackerRepository.save(tracker);
    }

//--------------------------------------------------------------------------------------------------------------

    private Tracker findTrackerByIdOrThrow(Long id) {
        return trackerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessages.RESOURCE_NOT_FOUND_BY_ID_MESSAGE, "Book", id)));
    }

    private boolean checkTrackerExistenceByBookId(Long bookId) {
        return trackerRepository.existsTrackerByBookId(bookId);
    }

    private void checkTrackerExistenceByBookIdAndThrow(Long bookId) {
        if (checkTrackerExistenceByBookId(bookId)) {
            throw new DuplicateResourceException(
                    String.format(ErrorMessages.DUPLICATE_RESOURCE_MESSAGE, "Book", "book ID"));
        }
    }

    private void checkTrackerExistenceByBookIdOrThrow(Long bookId) {
        if (!checkTrackerExistenceByBookId(bookId)) {
            throw new ResourceNotFoundException(
                    String.format(ErrorMessages.RESOURCE_NOT_FOUND_BY_FIELD_MESSAGE, "Book", "ID"));
        }
    }

}
