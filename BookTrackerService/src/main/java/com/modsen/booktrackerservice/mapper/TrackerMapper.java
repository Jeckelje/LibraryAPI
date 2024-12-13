package com.modsen.booktrackerservice.mapper;

import com.modsen.booktrackerservice.dto.request.CreateTrackerRequest;
import com.modsen.booktrackerservice.dto.request.UpdateStatusRequest;
import com.modsen.booktrackerservice.dto.response.TrackerResponse;
import com.modsen.booktrackerservice.model.Tracker;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TrackerMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "bookId", target = "bookId")
    @Mapping(source = "takeDate", target = "takeDate")
    @Mapping(source = "returnDate", target = "returnDate")
    TrackerResponse toTrackerResponse(Tracker tracker);

    Tracker toTracker(TrackerResponse trackerResponse);

    @Mapping(target = "status", defaultValue = "free")
    @Mapping(target = "isDeleted", defaultValue = "false")
    @Mapping(target = "takeDate", source = "createTrackerRequest.takeDate", defaultValue = "null")
    @Mapping(target = "returnDate", source = "createTrackerRequest.returnDate", defaultValue = "null")
    Tracker toTracker(CreateTrackerRequest createTrackerRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Tracker totracker(UpdateStatusRequest updateStatusRequest);

    @Mapping(target = "id", ignore = true)
    void updateTrackerFromRequest(UpdateStatusRequest updateStatusRequest, @MappingTarget Tracker tracker);

//    AppError toAppError();

}
