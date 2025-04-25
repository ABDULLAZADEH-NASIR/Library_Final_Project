package az.texnoera.library_management_system.model.mapper;

import az.texnoera.library_management_system.entity.Book;
import az.texnoera.library_management_system.model.enums.BookCategory;
import az.texnoera.library_management_system.model.request.BookRequest;
import az.texnoera.library_management_system.model.request.BookRequestForBookUpdate;
import az.texnoera.library_management_system.model.response.AuthorResponseForBook;
import az.texnoera.library_management_system.model.response.BookResponse;
import az.texnoera.library_management_system.model.response.BookResponseWithAuthors;
import az.texnoera.library_management_system.model.response.BookResponseWithBookCount;


import java.util.HashSet;
import java.util.stream.Collectors;


public interface BookMapper {

    static Book bookRequestToBook(BookRequest bookRequest) {

        return Book.builder()
                .name(bookRequest.getName())
                .authors(new HashSet<>())
                .bookCheckouts(new HashSet<>())
                .year(bookRequest.getYear())
                .pages(bookRequest.getPages())
                .totalBooksCount(bookRequest.getTotalBookCount())
                .avialableBooksCount(bookRequest.getAvailableBookCount())
                .build();
    }

    static BookResponseWithBookCount bookToBookResponseWithBookCount(Book book) {
        assert book.getCategory() != null;
        return BookResponseWithBookCount.builder()
                .id(book.getId())
                .name(book.getName())
                .bookCategory(book.getCategory().name())
                .pages(book.getPages())
                .totalBookCount(book.getTotalBooksCount())
                .availableBookCount(book.getAvialableBooksCount())
                .year(book.getYear())
                .authors(book.getAuthors().stream().map(author ->
                                AuthorResponseForBook.builder()
                                        .name(author.getName())
                                        .surname(author.getSurname())
                                        .build())
                        .collect(Collectors.toSet()))
                .build();
    }

    static BookResponseWithAuthors bookToBookResponseWithAuthors(Book book) {
        assert book.getCategory() != null;
        return BookResponseWithAuthors.builder()
                .id(book.getId())
                .name(book.getName())
                .bookCategory(book.getCategory().name())
                .pages(book.getPages())
                .availableBookCount(book.getAvialableBooksCount())
                .year(book.getYear())
                .authors(book.getAuthors().stream().map(author ->
                                AuthorResponseForBook.builder()
                                        .name(author.getName())
                                        .surname(author.getSurname())
                                        .build())
                        .collect(Collectors.toSet()))
                .build();
    }

    static BookResponse bookToBookResponse(Book book) {
        assert book.getCategory() != null;
        return BookResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .bookCategory(book.getCategory().name())
                .pages(book.getPages())
                .availableBookCount(book.getAvialableBooksCount())
                .year(book.getYear())
                .build();
    }

    static void bookUpdateToBook(Book book, BookRequestForBookUpdate bookRequest) {
        book.setName(bookRequest.getName());
        book.setYear(bookRequest.getYear());
        book.setPages(bookRequest.getPages());
        book.setTotalBooksCount(bookRequest.getTotalBookCount());
    }
}