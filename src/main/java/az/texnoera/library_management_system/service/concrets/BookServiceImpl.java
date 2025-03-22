package az.texnoera.library_management_system.service.concrets;

import az.texnoera.library_management_system.entity.Author;
import az.texnoera.library_management_system.entity.Book;
import az.texnoera.library_management_system.exception_Handle.BasedExceptions;
import az.texnoera.library_management_system.model.enums.BookCategory;
import az.texnoera.library_management_system.model.enums.StatusCode;
import az.texnoera.library_management_system.model.mapper.BookMapper;
import az.texnoera.library_management_system.model.request.BookRequest;
import az.texnoera.library_management_system.model.request.BookRequestForBookUpdate;
import az.texnoera.library_management_system.model.response.BookResponse;
import az.texnoera.library_management_system.model.response.Result;
import az.texnoera.library_management_system.repo.BookRepo;
import az.texnoera.library_management_system.service.abstracts.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepo bookRepo;

    @Transactional
    @Override
    public BookResponse createBook(BookRequest bookRequest) {
        Book book = BookMapper.BookRequestToBook(bookRequest);
        book = bookRepo.save(book);
        return BookMapper.BookToBookResponse(book);
    }

    @Override
    public Result<BookResponse> getAllBooks(int page, int size) {
        Pageable page1 = PageRequest.of(page, size);
        Page<Book> books = bookRepo.findAllBooks(page1);

        List<BookResponse> bookResponses = books.stream()
                .map(BookMapper::BookToBookResponse).toList();
        return new Result<>(bookResponses, page, size, books.getTotalPages());
    }

    @Override
    public BookResponse getBookById(Long id) {
        Book book = bookRepo.findBookById(id).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));
        return BookMapper.BookToBookResponse(book);
    }

    @Override
    public BookResponse getBookByBookName(String bookName) {
        Book book = bookRepo.findBookByName(bookName).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));
        return BookMapper.BookToBookResponse(book);
    }


    @Override
    public void deleteBookById(Long id) {
        Book book = bookRepo.findBookById(id).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));
        bookRepo.delete(book);
    }

    @Transactional
    @Override
    public BookResponse updateBookById(Long id, BookRequestForBookUpdate bookRequest) {
        Book book = bookRepo.findBookById(id).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));
        BookMapper.bookUpdateToBook(book, bookRequest);
        return BookMapper.BookToBookResponse(bookRepo.save(book));
    }

    @Override
    public Result<BookResponse> getBooksByBookCategory(String category, int page, int size) {
        String categoryFilter = category.toUpperCase();
        Pageable page3 = PageRequest.of(page, size);
        Page<Book> books = bookRepo.findABookByCategory(BookCategory.valueOf(categoryFilter), page3);

        List<BookResponse> bookResponses = books.stream()
                .map(BookMapper::BookToBookResponse).toList();
        return new Result<>(bookResponses, page, size, books.getTotalPages());
    }
}
