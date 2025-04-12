package az.texnoera.library_management_system.service.abstracts;


import az.texnoera.library_management_system.model.request.BookRequest;
import az.texnoera.library_management_system.model.request.BookRequestForBookUpdate;
import az.texnoera.library_management_system.model.response.BookResponse;
import az.texnoera.library_management_system.model.response.BookResponseWithAuthors;
import az.texnoera.library_management_system.model.response.BookResponseWithBookCount;
import az.texnoera.library_management_system.model.response.Result;


public interface BookService {
    Result<BookResponse> getAllBooks(int page, int size);

    BookResponseWithBookCount getBookWithCountById(Long id);

    BookResponseWithAuthors getBookByBookName(String bookName);

    BookResponseWithBookCount createBook(BookRequest bookRequest);

    void deleteBookById(Long id);

    BookResponseWithBookCount updateBookById(Long id, BookRequestForBookUpdate bookRequest);

    Result<BookResponse> getBooksByBookCategory(String category, int page, int size);

    BookResponseWithBookCount getBookById(Long id);
}
