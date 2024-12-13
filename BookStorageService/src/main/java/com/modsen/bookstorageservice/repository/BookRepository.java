package com.modsen.bookstorageservice.repository;

import com.modsen.bookstorageservice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Book getByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

}
