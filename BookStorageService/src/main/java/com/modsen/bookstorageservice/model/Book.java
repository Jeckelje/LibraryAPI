package com.modsen.bookstorageservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "books")
@FilterDef(name = "deletedFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedFilter", condition = "isDeleted = :isDeleted")
@Schema(description = "Book entity info")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id", example = "1")
    private Long id;

    @Column(nullable = false, name = "book_isbn", length = 50)
    @Schema(description = "Book ISBN", example = "ISBN 978-5-7320-1162-3")
    private String isbn;

    @Column(nullable = false, name = "book_title", length = 256)
    @Schema(description = "Book title", example = "Harry Potter")
    private String title;

    @Column(nullable = false, name = "book_genre", length = 256)
    @Schema(description = "Book genre", example = "Fantasy")
    private String genre;

    @Column(nullable = false, name = "book_description", length = 1024)
    @Schema(description = "Book description", example = "The book is about a boy who...")
    private String description;

    @Column(nullable = false, name = "book_author", length = 100)
    @Schema(description = "Book author", example = "J. K. Rowling")
    private String author;

    @Column(nullable = false, name = "is_deleted")
    @Schema(description = "is deleted", example = "false")
    private boolean isDeleted;

}
