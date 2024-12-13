package com.modsen.booktrackerservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "book_tracker")
@FilterDef(name = "deletedFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedFilter", condition = "isDeleted = :isDeleted")
@Schema(description = "Tracker entity info")
public class Tracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID", example = "1")
    private Long id;


    @Column(nullable = false, name = "book_id")
    @Schema(description = "Book ID", example = "4")
    private Long bookId;

    @Column(nullable = false, name = "status")
    @Schema(description = "Book status", example = "free")
    @Pattern(regexp = "^(free|taken)$")
    private String status;

    @Column(name = "taken")
    @Schema(description = "Book taken date", example = "2024-01-10", format = "date")
    //@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate takeDate;

    @Column(name = "returned")
    @Schema(description = "Book returned date", example = "2024-10-14", format = "date")
    //@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;

    @Column(nullable = false, name = "is_deleted")
    @Schema(description = "is deleted", example = "false")
    private boolean isDeleted;

}
