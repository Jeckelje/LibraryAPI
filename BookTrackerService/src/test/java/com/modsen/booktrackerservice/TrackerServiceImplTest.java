package com.modsen.booktrackerservice;

import com.modsen.booktrackerservice.dto.request.UpdateStatusRequest;
import com.modsen.booktrackerservice.dto.response.TrackerResponse;
import com.modsen.booktrackerservice.error.ErrorMessages;
import com.modsen.booktrackerservice.exception.DuplicateResourceException;
import com.modsen.booktrackerservice.exception.ResourceNotFoundException;
import com.modsen.booktrackerservice.mapper.TrackerMapper;
import com.modsen.booktrackerservice.model.Tracker;
import com.modsen.booktrackerservice.repository.TrackerRepository;
import com.modsen.booktrackerservice.service.Impl.TrackerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrackerServiceImplTest {

    @Mock
    private TrackerRepository trackerRepository;

    @Mock
    private TrackerMapper trackerMapper;

    @InjectMocks
    private TrackerServiceImpl trackerService;

    private Tracker mockTracker(Long id, Long bookId, String status, LocalDate takeDate, LocalDate returnDate, boolean isDeleted) {
        Tracker tracker = new Tracker();
        tracker.setId(id);
        tracker.setBookId(bookId);
        tracker.setStatus(status);
        tracker.setTakeDate(takeDate);
        tracker.setReturnDate(returnDate);
        tracker.setDeleted(isDeleted);
        return tracker;
    }

    private TrackerResponse mockTrackerResponse(Long id, Long bookId, String status, LocalDate takeDate, LocalDate returnDate) {
        return new TrackerResponse(id, bookId, status, takeDate.toString(), returnDate.toString());
    }

//    @Test
//    void createTracker_success() {
//        Long bookId = 1L;
//        Tracker tracker = mockTracker(1L, bookId, "free", LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-02"), false);
//        Tracker savedTracker = mockTracker(1L, bookId, "free", LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-01"), false);
//        TrackerResponse response = mockTrackerResponse(1L, bookId, "free", LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-02"));
//
//        when(trackerRepository.existsTrackerByBookId(bookId)).thenReturn(false);
//        doReturn(savedTracker).when(trackerRepository).save(any(Tracker.class));
//        when(trackerMapper.toTrackerResponse(savedTracker)).thenReturn(response);
//
//        //TrackerResponse result = trackerService.createTracker(bookId);
//
//        assertNotNull(result);
//        assertEquals(response, result);
//        verify(trackerRepository, times(1)).existsTrackerByBookId(bookId);
//        verify(trackerRepository, times(1)).save(any(Tracker.class));
//    }

//    @Test
//    void createTracker_duplicateTracker_throwsException() {
//        Long bookId = 1L;
//        when(trackerRepository.existsTrackerByBookId(bookId)).thenReturn(true);
//
//        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
//                () -> trackerService.createTracker(bookId));
//        assertEquals(String.format(ErrorMessages.DUPLICATE_RESOURCE_MESSAGE, "Book", "book ID"), exception.getMessage());
//        verify(trackerRepository, times(1)).existsTrackerByBookId(bookId);
//    }

    @Test
    void getAllTrackersWhereStatusIsFree_success() {
        Tracker tracker1 = mockTracker(1L, 1L, "free", LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-02"), false);
        Tracker tracker2 = mockTracker(2L, 2L, "free", LocalDate.parse("2024-01-03"), LocalDate.parse("2024-01-04"), false);
        TrackerResponse response1 = mockTrackerResponse(1L, 1L, "free", LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-02"));
        TrackerResponse response2 = mockTrackerResponse(2L, 2L, "free", LocalDate.parse("2024-01-03"), LocalDate.parse("2024-01-04"));

        when(trackerRepository.getAllByStatus("free")).thenReturn(List.of(tracker1, tracker2));
        when(trackerMapper.toTrackerResponse(tracker1)).thenReturn(response1);
        when(trackerMapper.toTrackerResponse(tracker2)).thenReturn(response2);

        List<TrackerResponse> result = trackerService.getAllTrackersWhereStatusIsFree();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));
        verify(trackerRepository, times(1)).getAllByStatus("free");
    }

    @Test
    void changeStatus_success() {
        Long bookId = 1L;
        Tracker tracker = mockTracker(1L, bookId, "free", LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-02"), false);
        Tracker updatedTracker = mockTracker(1L, bookId, "taken", LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-02"), false);
        TrackerResponse response = mockTrackerResponse(1L, bookId, "taken", LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-02"));
        UpdateStatusRequest request = new UpdateStatusRequest("taken");

        when(trackerRepository.existsTrackerByBookId(bookId)).thenReturn(true);
        when(trackerRepository.findByBookId(bookId)).thenReturn(tracker);
        when(trackerRepository.save(tracker)).thenReturn(updatedTracker);
        when(trackerMapper.toTrackerResponse(updatedTracker)).thenReturn(response);

        TrackerResponse result = trackerService.changeStatus(bookId, request);

        assertNotNull(result);
        assertEquals(response, result);
        verify(trackerRepository, times(1)).findByBookId(bookId);
        verify(trackerRepository, times(1)).save(tracker);
    }

    @Test
    void changeStatus_trackerNotFound_throwsException() {
        Long bookId = 1L;
        UpdateStatusRequest request = new UpdateStatusRequest("taken");
        when(trackerRepository.existsTrackerByBookId(bookId)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> trackerService.changeStatus(bookId, request));
        assertEquals(String.format(ErrorMessages.RESOURCE_NOT_FOUND_BY_FIELD_MESSAGE, "Book", "ID"), exception.getMessage());
        verify(trackerRepository, times(1)).existsTrackerByBookId(bookId);
    }

    @Test
    void softDeleteTrackerById_success() {
        Long trackerId = 1L;
        Tracker tracker = mockTracker(trackerId, 1L, "free", LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-02"), false);

        when(trackerRepository.findById(trackerId)).thenReturn(Optional.of(tracker));

        trackerService.softDeleteTrackerById(trackerId);

        assertTrue(tracker.isDeleted());
        verify(trackerRepository, times(1)).findById(trackerId);
        verify(trackerRepository, times(1)).save(tracker);
    }

    @Test
    void softDeleteTrackerById_notFound_throwsException() {
        Long trackerId = 1L;
        when(trackerRepository.findById(trackerId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> trackerService.softDeleteTrackerById(trackerId));
        assertEquals(String.format(ErrorMessages.RESOURCE_NOT_FOUND_BY_ID_MESSAGE, "Book", trackerId), exception.getMessage());
        verify(trackerRepository, times(1)).findById(trackerId);
    }

    @Test
    void softDeleteTrackerByBookId_success() {
        Long bookId = 1L;
        Tracker tracker = mockTracker(1L, bookId, "free", LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-02"), false);

        when(trackerRepository.existsTrackerByBookId(bookId)).thenReturn(true);
        when(trackerRepository.findByBookId(bookId)).thenReturn(tracker);

        trackerService.softDeleteTrackerByBookId(bookId);

        assertTrue(tracker.isDeleted());
        verify(trackerRepository, times(1)).findByBookId(bookId);
        verify(trackerRepository, times(1)).save(tracker);
    }

    @Test
    void softDeleteTrackerByBookId_notFound_throwsException() {
        Long bookId = 1L;
        when(trackerRepository.existsTrackerByBookId(bookId)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> trackerService.softDeleteTrackerByBookId(bookId));
        assertEquals(String.format(ErrorMessages.RESOURCE_NOT_FOUND_BY_FIELD_MESSAGE, "Book", "ID"), exception.getMessage());
        verify(trackerRepository, times(1)).existsTrackerByBookId(bookId);
    }

}
