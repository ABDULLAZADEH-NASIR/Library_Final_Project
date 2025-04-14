package az.texnoera.library_management_system.service.concrets;

import az.texnoera.library_management_system.entity.Book;
import az.texnoera.library_management_system.exception_Handle.BasedExceptions;
import az.texnoera.library_management_system.model.enums.BookCategory;
import az.texnoera.library_management_system.model.enums.StatusCode;
import az.texnoera.library_management_system.model.mapper.BookMapper;
import az.texnoera.library_management_system.model.request.BookRequest;
import az.texnoera.library_management_system.model.request.BookRequestForBookUpdate;
import az.texnoera.library_management_system.model.response.BookResponse;
import az.texnoera.library_management_system.model.response.BookResponseWithAuthors;
import az.texnoera.library_management_system.model.response.BookResponseWithBookCount;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepo bookRepo;

    // Yeni book yaradır
    @Transactional
    @Override
    public BookResponseWithBookCount createBook(BookRequest bookRequest) {
        Book book = BookMapper.bookRequestToBook(bookRequest);
        book = bookRepo.save(book);
        return BookMapper.bookToBookResponseWithBookCount(book);
    }

    // Bütün bookları gətirir
    @Override
    public Result<BookResponse> getAllBooks(int page, int size) {
        Pageable page1 = PageRequest.of(page, size);
        Page<Book> books = bookRepo.findAllBooks(page1);

        List<BookResponse> bookResponses = books.stream()
                .map(BookMapper::bookToBookResponse).toList();
        return new Result<>(bookResponses, page, size, books.getTotalPages());
    }

    // Book id ilə və book sayı da göstərilməklə göstərilir
    @Override
    public BookResponseWithBookCount getBookWithCountById(Long id) {
        Book book = bookRepo.findBookById(id).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));
        return BookMapper.bookToBookResponseWithBookCount(book);
    }


    // Book id ilə göstərir
    @Override
    public BookResponseWithBookCount getBookById(Long id) {
        Book book = bookRepo.findBookById(id).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));
        return BookMapper.bookToBookResponseWithBookCount(book);
    }


    // Book adına görə gostərir
    @Override
    public BookResponseWithAuthors getBookByBookName(String bookName) {
        Book book = bookRepo.findBookByName(bookName).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));
        return BookMapper.bookToBookResponseWithAuthors(book);
    }


    // Book id ilə silir
    @Override
    public void deleteBookById(Long id) {
        Book book = bookRepo.findBookById(id).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));
        bookRepo.delete(book);
    }

    // Book update edir
    @Transactional
    @Override
    public BookResponseWithBookCount updateBookById(Long id, BookRequestForBookUpdate bookRequest) {
        Book book = bookRepo.findBookById(id).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));
        BookMapper.bookUpdateToBook(book, bookRequest);
        return BookMapper.bookToBookResponseWithBookCount(bookRepo.save(book));
    }

    // Kateqoriyaya görə bookları göstərir
    @Override
    public Result<BookResponse> getBooksByBookCategory(String category, int page, int size) {
        String categoryFilter = category.toUpperCase();
        Pageable page3 = PageRequest.of(page, size);
        Page<Book> books = bookRepo.findABookByCategory(BookCategory.valueOf(categoryFilter), page3);
        List<BookResponse> bookResponses = books.stream()
                .map(BookMapper::bookToBookResponse).toList();
        return new Result<>(bookResponses, page, size, books.getTotalPages());
    }
}
