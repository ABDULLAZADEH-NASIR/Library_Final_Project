package az.texnoera.library_management_system.model.mapper;

import az.texnoera.library_management_system.entity.Author;
import az.texnoera.library_management_system.entity.Book;
import az.texnoera.library_management_system.model.request.BookRequest;
import az.texnoera.library_management_system.model.response.AuthorResponse;
import az.texnoera.library_management_system.model.response.BookResponse;

import java.util.HashSet;
import java.util.stream.Collectors;


public interface BookMapper {


    static Book BookRequestToBook(BookRequest bookRequest) {

        return Book.builder()
                .name(bookRequest.getName())
                .authors(new HashSet<>())
                .borrowBook(new HashSet<>())
                .year(bookRequest.getYear())
                .pages(bookRequest.getPages())
                .count(bookRequest.getCount())
                .category(bookRequest.getCategory())
                .build();
    }

    static BookResponse BookToBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .authors(book.getAuthors().stream().map(author ->
                        AuthorResponse.builder()
                                .name(author.getName())
                                .surname(author.getSurname())
                                .build()).collect(Collectors.toSet()))
                .bookCategory(book.getCategory())
                .pages(book.getPages())
                .count(book.getCount())
                .year(book.getYear())
                .build();

    }
}
