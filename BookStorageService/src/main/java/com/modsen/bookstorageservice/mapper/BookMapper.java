package com.modsen.bookstorageservice.mapper;


import com.modsen.bookstorageservice.dto.request.CreateBookRequest;
import com.modsen.bookstorageservice.dto.request.UpdateBookRequest;
import com.modsen.bookstorageservice.dto.response.BookResponse;
import com.modsen.bookstorageservice.model.Book;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookResponse toBookResponse(Book book);

    Book toBook(BookResponse bookResponse);

    Book toBook(CreateBookRequest createBookRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Book toBook(UpdateBookRequest updateBookRequest);

    @Mapping(target = "id", ignore = true)
    void updateBookFromRequest(UpdateBookRequest updateBookRequest, @MappingTarget Book book);

}
