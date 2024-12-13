package com.modsen.bookstorageservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Create book request DTO")
public record CreateBookRequest(

        @NotNull(message = "{book.isbn.notnull}")
        @Size(min = 2, max = 50, message = "{book.isbn.size}")
        @Schema(description = "Book ISBN", example = "ISBN 978-5-7320-1162-3")
        String isbn,

        @NotNull(message = "{book.title.notnull}")
        @Size(min = 2, max = 256, message = "{book.title.size}")
        @Schema(description = "Book title", example = "Harry Potter")
        String title,

        @NotNull(message = "{book.genre.notnull}")
        @Size(min = 2, max = 256, message = "{book.genre.size}")
        @Schema(description = "Book genre", example = "Fantasy")
        String genre,

        @NotNull(message = "{book.description.notnull}")
        @Size(min = 2, max = 1024, message = "{book.description.size}")
        @Schema(description = "Book description", example = "The book is about a boy who...")
        String description,

        @NotNull(message = "{book.author.notnull}")
        @Size(min = 2, max = 100, message = "{book.author.size}")
        @Schema(description = "Book author", example = "J. K. Rowling")
        String author

) {
}
