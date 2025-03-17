package az.texnoera.library_management_system.model.mapper;

import az.texnoera.library_management_system.entity.Author;
import az.texnoera.library_management_system.entity.Book;
import az.texnoera.library_management_system.model.enums.BookCategory;
import az.texnoera.library_management_system.model.request.BookRequest;
import az.texnoera.library_management_system.model.response.AuthorResponse;
import az.texnoera.library_management_system.model.response.BookResponse;

import java.util.HashSet;
import java.util.stream.Collectors;


public interface BookMapper {

    static Book BookRequestToBook(BookRequest bookRequest) {

        if (bookRequest.getCategory() == null || bookRequest.getCategory().isBlank()) {
            bookRequest.setCategory("DETECTIVE");  // Default olaraq "DETECTIVE" təyin etdim
        }

        // bookRequest-dən category-nin enum dəyərinə çevrilməsi
        BookCategory category;
        try {

            category = BookCategory.valueOf(bookRequest.getCategory().trim().toUpperCase());  // Enum-a çevrilir
        } catch (IllegalArgumentException e) {
            // Əgər yanlış category gəlirsə, default olaraq "DETECTIVE" təyin etdim
            category = BookCategory.DETECTIVE;
        }

        return Book.builder()
                .name(bookRequest.getName())
                .authors(new HashSet<>())
                .borrowBook(new HashSet<>())
                .year(bookRequest.getYear())
                .pages(bookRequest.getPages())
                .totalBooksCount(bookRequest.getTotalBookCount())
                .avialableBooksCount(bookRequest.getAvailableBookCount())
                .category(category)
                .build();
    }

    static BookResponse BookToBookResponse(Book book) {
        assert book.getCategory() != null;
        return BookResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .authors(book.getAuthors().stream().map(author ->
                                AuthorResponse.builder()
                                        .name(author.getName())
                                        .surname(author.getSurname())
                                        .build())
                        .collect(Collectors.toSet()))
                .bookCategory(book.getCategory().name())
                .pages(book.getPages())
                .totalBookCount(book.getTotalBooksCount())
                .availableBookCount(book.getAvialableBooksCount())
                .year(book.getYear())
                .build();
    }
}