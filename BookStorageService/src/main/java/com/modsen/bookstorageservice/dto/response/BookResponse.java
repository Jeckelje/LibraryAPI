package com.modsen.bookstorageservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Book response DTO")
public record BookResponse(

        @Schema(description = "Id", example = "1")
        Long id,

        @Schema(description = "Book ISBN", example = "ISBN 978-5-7320-1162-3")
        String isbn,

        @Schema(description = "Book title", example = "Harry Potter")
        String title,

        @Schema(description = "Book genre", example = "Fantasy")
        String genre,

        @Schema(description = "Book description", example = "The book is about a boy who...")
        String description,

        @Schema(description = "Book author", example = "J. K. Rowling")
        String author

) {
}
