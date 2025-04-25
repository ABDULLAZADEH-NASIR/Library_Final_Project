package az.texnoera.library_management_system.controller;

import az.texnoera.library_management_system.model.response.*;
import az.texnoera.library_management_system.service.concrets.AuthorServiceImpl;
import az.texnoera.library_management_system.service.concrets.BookServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/public")
// Public olan APİ-lar burdadır
public class PublicController {

    private final AuthorServiceImpl authorService;
    private final BookServiceImpl bookService;

    // Bütün authorları göstərir
    @GetMapping("/authors")
    @ResponseStatus(HttpStatus.OK)  // 200
    public Result<AuthorResponse> getAllAuthors(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("GET /v1/public/authors/all called with page={}, size={}", page, size);
        return authorService.getAllAuthors(page, size);
    }

    // Authoru adı ilə axtarır
    @GetMapping("/authors/search-by-name/")
    @ResponseStatus(HttpStatus.OK)  // 200
    public AuthorResponseWithBooks getAuthorByName(@RequestParam String name) {
        log.info("GET /v1/public/authors/search-by-name called with name={}", name);
        return authorService.getAuthorByAuthorName(name);
    }

    // Bütün bookları gətirir
    @GetMapping("/books")
    @ResponseStatus(HttpStatus.OK)  // 200
    public Result<BookResponse> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /v1/public/books/all called with page={}, size={}", page, size);
        return bookService.getAllBooks(page, size);
    }

    // Kateqoriyaya görə bookları göstərir
    @GetMapping("/books/search-by-bookCategory/{category}")
    @ResponseStatus(HttpStatus.OK)  // 200
    public Result<BookResponse> getBooksByBookCategory(@PathVariable String category,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        log.info("GET /v1/public/books/search-by-bookCategory/{} called with page={}, size={}", category, page, size);
        return bookService.getBooksByBookCategory(category, page, size);
    }

    // Book adına görə gostərir
    @GetMapping("/books/search-by-name/{bookName}")
    @ResponseStatus(HttpStatus.OK)  // 200
    public BookResponseWithAuthors getBookByName(@PathVariable String bookName) {
        log.info("GET /v1/public/books/search-by-name/{} called", bookName);
        return bookService.getBookByBookName(bookName);
    }
}