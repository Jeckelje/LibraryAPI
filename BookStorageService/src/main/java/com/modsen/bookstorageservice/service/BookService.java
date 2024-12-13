package com.modsen.bookstorageservice.service;

import com.modsen.bookstorageservice.dto.request.CreateBookRequest;
import com.modsen.bookstorageservice.dto.request.UpdateBookRequest;
import com.modsen.bookstorageservice.dto.response.BookResponse;

import java.util.List;

public interface BookService {

    BookResponse createBook(CreateBookRequest createBookRequest);

    BookResponse updateBook(Long id, UpdateBookRequest updateBookRequest);

    void softDeleteBook(Long id);

    BookResponse getBookById(Long id);

    List<BookResponse> getAllBooks();

    BookResponse getBookByISBN(String isbn);

}
