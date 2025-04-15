package az.texnoera.library_management_system.controller;

import az.texnoera.library_management_system.model.response.*;
import az.texnoera.library_management_system.service.concrets.AuthorServiceImpl;
import az.texnoera.library_management_system.service.concrets.BookServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/public")
// Public olan APİ-lar burdadır
public class PublicController {

    private final AuthorServiceImpl authorService;
    private final BookServiceImpl bookService;
    private static final Logger logger = LoggerFactory.getLogger(PublicController.class);

    // Bütün authorları göstərir
    @GetMapping("/authors/all")
    public Result<AuthorResponse> getAllAuthors(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        logger.info("GET /v1/public/authors/all called with page={}, size={}", page, size);
        return authorService.getAllAuthors(page, size);
    }

    // Authoru adı ilə axtarır
    @GetMapping("/authors/search-by-name/")
    public AuthorResponseWithBooks getAuthorByName(@RequestParam String name) {
        logger.info("GET /v1/public/authors/search-by-name called with name={}", name);
        return authorService.getAuthorByAuthorName(name);
    }

    // Bütün bookları gətirir
    @GetMapping("/books/all")
    public Result<BookResponse> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        logger.info("GET /v1/public/books/all called with page={}, size={}", page, size);
        return bookService.getAllBooks(page, size);
    }

    // Kateqoriyaya görə bookları göstərir
    @GetMapping("/books/search-by-bookCategory/{category}")
    public Result<BookResponse> getBooksByBookCategory(@PathVariable String category,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        logger.info("GET /v1/public/books/search-by-bookCategory/{} called with page={}, size={}", category, page, size);
        return bookService.getBooksByBookCategory(category, page, size);
    }

    // Book adına görə gostərir
    @GetMapping("/books/search-by-name/{bookName}")
    public BookResponseWithAuthors getBookByName(@PathVariable String bookName) {
        logger.info("GET /v1/public/books/search-by-name/{} called", bookName);
        return bookService.getBookByBookName(bookName);
    }
}