package com.modsen.bookstorageservice.service.impl;

import com.modsen.bookstorageservice.config.RabbitMQConfig;
import com.modsen.bookstorageservice.dto.request.CreateBookRequest;
import com.modsen.bookstorageservice.dto.request.UpdateBookRequest;
import com.modsen.bookstorageservice.dto.response.BookResponse;
import com.modsen.bookstorageservice.error.ErrorMessages;
import com.modsen.bookstorageservice.exception.DuplicateResourceException;
import com.modsen.bookstorageservice.exception.ResourceNotFoundException;
import com.modsen.bookstorageservice.mapper.BookMapper;
import com.modsen.bookstorageservice.model.Book;
import com.modsen.bookstorageservice.repository.BookRepository;
import com.modsen.bookstorageservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final RabbitTemplate rabbitTemplate;


    @Override
    public BookResponse createBook(CreateBookRequest createBookRequest) {
        checkBookExistenceAndThrow(createBookRequest.isbn());

        Book book = bookMapper.toBook(createBookRequest);
        Book savedBook = bookRepository.save(book);

        Long savedBookId = savedBook.getId();

        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, savedBookId);
        System.out.println("Sent message: " + savedBookId);

        return bookMapper.toBookResponse(savedBook);
    }

    @Override
    public BookResponse updateBook(Long id, UpdateBookRequest updateBookRequest) {
        Book existingBook = findBookByIdOrThrow(id);

        if (!existingBook.getIsbn().equals(updateBookRequest.isbn())) {
            if (bookRepository.existsByIsbn(updateBookRequest.isbn())) {
                throw new DuplicateResourceException(
                        String.format(ErrorMessages.DUPLICATE_RESOURCE_MESSAGE, "Book", "ISBN"));
            }
        }

        bookMapper.updateBookFromRequest(updateBookRequest, existingBook);
        Book updatedBook = bookRepository.save(existingBook);
        return bookMapper.toBookResponse(updatedBook);
    }

    @Override
    public void softDeleteBook(Long id) {
        Book book = findBookByIdOrThrow(id);
        book.setDeleted(true);
        bookRepository.save(book);
    }

    @Override
    public BookResponse getBookById(Long id) {
        Book book = findBookByIdOrThrow(id);
        return bookMapper.toBookResponse(book);
    }

    @Override
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookResponse)
                .toList();
    }

    @Override
    public BookResponse getBookByISBN(String isbn) {
        checkBookExistenceOrThrow(isbn);
        return bookMapper.toBookResponse(bookRepository.getByIsbn(isbn));
    }


    //-----------------------------------------------------------------------------------------------------------------------------

    private Book findBookByIdOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessages.RESOURCE_NOT_FOUND_BY_ID_MESSAGE, "Book", id)));
    }

    private boolean checkBookExistenceByISBN(String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }

    private void checkBookExistenceAndThrow(String bookISBN) {
        if (checkBookExistenceByISBN(bookISBN)) {
            throw new DuplicateResourceException(
                    String.format(ErrorMessages.DUPLICATE_RESOURCE_MESSAGE, "Book", "ISBN"));
        }
    }

    private void checkBookExistenceOrThrow(String bookIsbn) {
        if (!checkBookExistenceByISBN(bookIsbn)) {
            throw new ResourceNotFoundException(
                    String.format(ErrorMessages.RESOURCE_NOT_FOUND_BY_FIELD_MESSAGE, "Book", bookIsbn));
        }
    }

}
