package az.texnoera.library_management_system.service.abstracts;


import az.texnoera.library_management_system.entity.Book;
import az.texnoera.library_management_system.model.request.BookRequest;
import az.texnoera.library_management_system.model.request.BookRequestForBookUpdate;
import az.texnoera.library_management_system.model.response.BookResponse;
import az.texnoera.library_management_system.model.response.Result;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface BookService {
    Result<BookResponse> getAllBooks(int page, int size);
    BookResponse getBookById(Long id);
    BookResponse getBookByBookName(String bookName);
    BookResponse createBook(BookRequest bookRequest);
    void deleteBookById(Long id);
    BookResponse updateBookById(Long id, BookRequestForBookUpdate bookRequest);
    Result<BookResponse> getBooksByBookCategory(String category, int page, int size);
}
