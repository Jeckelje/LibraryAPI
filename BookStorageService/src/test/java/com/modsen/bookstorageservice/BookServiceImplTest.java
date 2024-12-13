package com.modsen.bookstorageservice;

import com.modsen.bookstorageservice.dto.request.CreateBookRequest;
import com.modsen.bookstorageservice.dto.request.UpdateBookRequest;
import com.modsen.bookstorageservice.dto.response.BookResponse;
import com.modsen.bookstorageservice.error.ErrorMessages;
import com.modsen.bookstorageservice.exception.DuplicateResourceException;
import com.modsen.bookstorageservice.exception.ResourceNotFoundException;
import com.modsen.bookstorageservice.mapper.BookMapper;
import com.modsen.bookstorageservice.model.Book;
import com.modsen.bookstorageservice.repository.BookRepository;
import com.modsen.bookstorageservice.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void createBook_success() {
        // Arrange
        CreateBookRequest request = new CreateBookRequest("Updated ISBN", "Updated Title", "Updated genre", "Updated description", "Updated author");
        Book book = new Book(1L, "12345", "Test Title", "Test genre", "Test description", "Test Author", false);
        BookResponse response = new BookResponse(1L, "12345", "Test Title", "Test genre", "Test description", "Test Author");

        String exchangeName = "bookExchange";

        when(bookRepository.existsByIsbn(request.isbn())).thenReturn(false);
        when(bookMapper.toBook(request)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toBookResponse(book)).thenReturn(response);

        // Act
        BookResponse result = bookService.createBook(request);

        // Assert
        assertNotNull(result);
        assertEquals(response, result);
        verify(bookRepository, times(1)).existsByIsbn(request.isbn());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void createBook_duplicateIsbn_throwsException() {
        // Arrange
        CreateBookRequest request = new CreateBookRequest("12345", "Test Title", "Test genre", "Test description", "Test Author");
        when(bookRepository.existsByIsbn(request.isbn())).thenReturn(true);

        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> bookService.createBook(request));
        assertEquals(String.format(ErrorMessages.DUPLICATE_RESOURCE_MESSAGE, "Book", "ISBN"), exception.getMessage());
        verify(bookRepository).existsByIsbn(request.isbn());
    }

    @Test
    void updateBook_success() {
        // Arrange
        Long bookId = 1L;
        UpdateBookRequest request = new UpdateBookRequest("Updated ISBN", "Updated Title", "Updated genre", "Updated description", "Updated author");
        Book book = new Book(1L, "12345", "Test Title", "Test genre", "Test description", "Test Author", false);
        Book updatedBook = new Book(1L, "12345", "Updated Title", "Updated genre", "Updated description", "Updated Author", false);
        BookResponse response = new BookResponse(1L, "12345", "Test Title", "Test genre", "Test description", "Test Author");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(updatedBook);
        when(bookMapper.toBookResponse(updatedBook)).thenReturn(response);

        // Act
        BookResponse result = bookService.updateBook(bookId, request);

        // Assert
        assertNotNull(result);
        assertEquals(response, result);
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(book);
    }

    @Test
    void updateBook_notFound_throwsException() {
        // Arrange
        Long bookId = 1L;
        UpdateBookRequest request = new UpdateBookRequest("Updated ISBN", "Updated Title", "Updated genre", "Updated description", "Updated author");
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> bookService.updateBook(bookId, request));
        assertEquals(String.format(ErrorMessages.RESOURCE_NOT_FOUND_BY_ID_MESSAGE, "Book", bookId), exception.getMessage());
        verify(bookRepository).findById(bookId);
    }

    @Test
    void softDeleteBook_success() {
        // Arrange
        Long bookId = 1L;
        Book book = new Book(1L, "12345", "Test Title", "Test genre", "Test description", "Test Author", false);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // Act
        bookService.softDeleteBook(bookId);

        // Assert
        assertTrue(book.isDeleted());
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(book);
    }

    @Test
    void getBookById_success() {
        // Arrange
        Long bookId = 1L;
        Book book = new Book(bookId, "12345", "Test Title", "Test genre", "Test description", "Test Author", false);
        BookResponse response = new BookResponse(bookId, "12345", "Test Title", "Test genre", "Test description", "Test Author");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toBookResponse(book)).thenReturn(response);

        // Act
        BookResponse result = bookService.getBookById(bookId);

        // Assert
        assertNotNull(result);
        assertEquals(response, result);
        verify(bookRepository).findById(bookId);
    }

    @Test
    void getBookById_notFound_throwsException() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> bookService.getBookById(bookId));
        assertEquals(String.format(ErrorMessages.RESOURCE_NOT_FOUND_BY_ID_MESSAGE, "Book", bookId), exception.getMessage());
        verify(bookRepository).findById(bookId);
    }

    @Test
    void getBookByISBN_success() {
        // Arrange
        String isbn = "12345";
        Book book = new Book(1L, isbn, "Test Title", "Test genre", "Test description", "Test Author", false);
        BookResponse response = new BookResponse(1L, "12345", "Test Title", "Test genre", "Test description", "Test Author");

        when(bookRepository.existsByIsbn(isbn)).thenReturn(true);
        when(bookRepository.getByIsbn(isbn)).thenReturn(book);
        when(bookMapper.toBookResponse(book)).thenReturn(response);

        // Act
        BookResponse result = bookService.getBookByISBN(isbn);

        // Assert
        assertNotNull(result);
        assertEquals(response, result);
        verify(bookRepository).existsByIsbn(isbn);
        verify(bookRepository).getByIsbn(isbn);
    }

    @Test
    void getBookByISBN_notFound_throwsException() {
        // Arrange
        String isbn = "12345";
        when(bookRepository.existsByIsbn(isbn)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> bookService.getBookByISBN(isbn));
        assertEquals(String.format(ErrorMessages.RESOURCE_NOT_FOUND_BY_FIELD_MESSAGE, "Book", isbn), exception.getMessage());
        verify(bookRepository).existsByIsbn(isbn);
    }

    @Test
    void getAllBooks_success() {
        // Arrange
        Book book1 = new Book(1L, "12345", "Test Title 1", "Test genre 1", "Test description 1", "Test Author 1", false);
        Book book2 = new Book(2L, "67890", "Test Title 2", "Test genre 2", "Test description 2", "Test Author 2", false);
        BookResponse response1 = new BookResponse(1L, "12345", "Test Title 1", "Test genre 1", "Test description 1", "Test Author 1");
        BookResponse response2 = new BookResponse(2L, "67890", "Test Title 2", "Test genre 2", "Test description 2", "Test Author 2");

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));
        when(bookMapper.toBookResponse(book1)).thenReturn(response1);
        when(bookMapper.toBookResponse(book2)).thenReturn(response2);

        // Act
        List<BookResponse> result = bookService.getAllBooks();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));
        verify(bookRepository).findAll();
    }
}

