package com.modsen.bookstorageservice.controller;

import com.modsen.bookstorageservice.dto.request.CreateBookRequest;
import com.modsen.bookstorageservice.dto.request.UpdateBookRequest;
import com.modsen.bookstorageservice.dto.response.BookResponse;
import com.modsen.bookstorageservice.service.BookService;
import com.modsen.bookstorageservice.swagger.BookAPI;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/books")
public class BookController implements BookAPI {

    private final BookService bookService;

    @PostMapping("/create-book")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public BookResponse createBook(@RequestBody @Valid CreateBookRequest createBookRequest) {
        return bookService.createBook(createBookRequest);
    }

    @PutMapping("/update-book/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public BookResponse updateBook(
            @PathVariable Long id,
            @RequestBody @Valid UpdateBookRequest updateBookRequest) {
        return bookService.updateBook(id, updateBookRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void softDeleteBook(@PathVariable Long id) {
        bookService.softDeleteBook(id);
    }

    @GetMapping("/get-by-id/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    @Override
    public BookResponse getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    @Override
    public List<BookResponse> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/isbn/{isbn}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    @Override
    public BookResponse getBookByIsbn(@PathVariable String isbn) {
        return bookService.getBookByISBN(isbn);
    }
}
