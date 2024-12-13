package com.modsen.booktrackerservice.repository;

import com.modsen.booktrackerservice.model.Tracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackerRepository extends JpaRepository<Tracker, Long> {
    boolean existsTrackerByBookId(Long bookId);

    Tracker findByBookId(Long bookId);

    List<Tracker> getAllByStatus(String status);

}
