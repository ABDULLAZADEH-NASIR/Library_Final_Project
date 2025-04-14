package az.texnoera.library_management_system.controller;

import az.texnoera.library_management_system.model.response.*;
import az.texnoera.library_management_system.service.concrets.AuthorServiceImpl;
import az.texnoera.library_management_system.service.concrets.BookServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/public")
// Public olan APİ-lar burdadır
public class PublicController {
    private final AuthorServiceImpl authorService;
    private final BookServiceImpl bookService;

    // Bütün authorları göstərir
    @GetMapping("/authors/all")
    public Result<AuthorResponse> getAllAuthors(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return authorService.getAllAuthors(page, size);
    }

    // Authoru adı ilə axtarır
    @GetMapping("/authors/search-by-name/")
    public AuthorResponseWithBooks getAuthorByName(@RequestParam String name) {
        return authorService.getAuthorByAuthorName(name);
    }

    // Bütün bookları gətirir
    @GetMapping("/books/all")
    public Result<BookResponse> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return bookService.getAllBooks(page, size);
    }

    // Kateqoriyaya görə bookları göstərir
    @GetMapping("/books/search-by-bookCategory/{category}")
    public Result<BookResponse> getBooksByBookCategory(@PathVariable String category,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return bookService.getBooksByBookCategory(category, page, size);
    }

    // Book adına görə gostərir
    @GetMapping("/books/search-by-name/{bookName}")
    public BookResponseWithAuthors getBookByName(@PathVariable String bookName) {
        return bookService.getBookByBookName(bookName);
    }
}
