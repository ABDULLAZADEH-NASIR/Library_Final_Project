package az.texnoera.library_management_system.service.concrets;

import az.texnoera.library_management_system.entity.Book;
import az.texnoera.library_management_system.exception.ApiException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepo bookRepo;


    // Yeni book yaradır
    @Transactional
    @Override
    public BookResponseWithBookCount createBook(BookRequest bookRequest) {
        log.info("Creating a new book with name: {}", bookRequest.getName());

        // Book kateqoriya enumdır ve daxil edilən kateqoriyanın Category enumında olub-olmadığını yoxlayır
        if (bookRequest.getCategory() == null || bookRequest.getCategory().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, StatusCode.CATEGORY_MISSING); // Categoriya kimi bos falan data gelse iwe duwecek
        }

        // Enum deyeri yaradiriq
        BookCategory category;
        try {
            // category deyerinin tam uygun wekilde enum ile muqayise edirik
            category = BookCategory.valueOf(bookRequest.getCategory().trim()); // Exact match tələb olunur, kiçik/böyük fərqinə baxılır
        } catch (IllegalArgumentException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, StatusCode.CATEGORY_NOT_FOUND); // Uygun deyer tapilmadiqda exception atilir
        }


        Book book = BookMapper.bookRequestToBook(bookRequest);
        book.setCategory(category);
        book = bookRepo.save(book);
        log.info("Book created successfully with ID: {}", book.getId());
        return BookMapper.bookToBookResponseWithBookCount(book);
    }

    // Bütün bookları gətirir
    @Override
    public Result<BookResponse> getAllBooks(int page, int size) {
        log.info("Fetching all books - page: {}, size: {}", page, size);
        Pageable page1 = PageRequest.of(page, size);
        Page<Book> books = bookRepo.findAllBooks(page1);

        List<BookResponse> bookResponses = books.stream()
                .map(BookMapper::bookToBookResponse).toList();
        log.info("Fetched {} books", bookResponses.size());
        return new Result<>(bookResponses, page, size, books.getTotalPages());
    }

    // Book id ilə və book sayı da göstərilməklə göstərilir
    @Override
    public BookResponseWithBookCount getBookWithCountById(Long id) {
        log.info("Fetching book with count by ID: {}", id);
        Book book = bookRepo.findBookById(id).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));
        log.info("Book found - ID: {}", book.getId());
        return BookMapper.bookToBookResponseWithBookCount(book);
    }

    // Book id ilə göstərir
    @Override
    public BookResponseWithBookCount getBookById(Long id) {
        log.info("Fetching book by ID: {}", id);
        Book book = bookRepo.findBookById(id).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));
        log.info("Book found - ID: {}", book.getId());
        return BookMapper.bookToBookResponseWithBookCount(book);
    }

    // Book adına görə gostərir
    @Override
    public BookResponseWithAuthors getBookByBookName(String bookName) {
        log.info("Fetching book by name: {}", bookName);
        Book book = bookRepo.findBookByName(bookName).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));
        log.info("Book found - Name: {}", book.getName());
        return BookMapper.bookToBookResponseWithAuthors(book);
    }

    // Book id ilə silir
    @Override
    public void deleteBookById(Long id) {
        log.info("Deleting book by ID: {}", id);
        Book book = bookRepo.findBookById(id).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));
        bookRepo.delete(book);
        log.info("Book deleted successfully - ID: {}", id);
    }

    // Book update edir
    @Transactional
    @Override
    public BookResponseWithBookCount updateBookById(Long id, BookRequestForBookUpdate bookRequest) {
        log.info("Updating book by ID: {}", id);
        Book book = bookRepo.findBookById(id).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));
        BookMapper.bookUpdateToBook(book, bookRequest);
        Book updatedBook = bookRepo.save(book);
        log.info("Book updated successfully - ID: {}", updatedBook.getId());
        return BookMapper.bookToBookResponseWithBookCount(updatedBook);
    }

    // Kateqoriyaya görə bookları göstərir
    @Override
    public Result<BookResponse> getBooksByBookCategory(String category, int page, int size) {
        String categoryFilter = category.toUpperCase();
        log.info("Fetching books by category: {}, page: {}, size: {}", categoryFilter, page, size);
        Pageable page3 = PageRequest.of(page, size);
        Page<Book> books = bookRepo.findABookByCategory(BookCategory.valueOf(categoryFilter), page3);
        List<BookResponse> bookResponses = books.stream()
                .map(BookMapper::bookToBookResponse).toList();
        log.info("Fetched {} books for category {}", bookResponses.size(), categoryFilter);
        return new Result<>(bookResponses, page, size, books.getTotalPages());
    }
}