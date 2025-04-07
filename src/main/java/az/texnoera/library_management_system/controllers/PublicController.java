package az.texnoera.library_management_system.controllers;

import az.texnoera.library_management_system.model.request.UserRequest;
import az.texnoera.library_management_system.model.response.AuthorResponse;
import az.texnoera.library_management_system.model.response.BookResponse;
import az.texnoera.library_management_system.model.response.Result;
import az.texnoera.library_management_system.service.concrets.AuthorServiceImpl;
import az.texnoera.library_management_system.service.concrets.BookServiceImpl;
import az.texnoera.library_management_system.service.concrets.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/public")
public class PublicController {
    private final AuthorServiceImpl authorService;
    private final BookServiceImpl bookService;
    private final UserServiceImpl userService;


    @GetMapping("/all-authors")
    public Result<AuthorResponse> getAllAuthors(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return authorService.getAllAuthors(page, size);
    }

    @GetMapping("/search/author-by-name/")
    public AuthorResponse getAuthorByName(@RequestParam String name) {
        return authorService.getAuthorByAuthorName(name);
    }

    @GetMapping("all-books")
    public Result<BookResponse> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return bookService.getAllBooks(page, size);

    }

    @GetMapping("/search-book/bookCategory/{category}")
    public Result<BookResponse> getBooksByBookCategory(@PathVariable String category,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return bookService.getBooksByBookCategory(category, page, size);
    }

    @GetMapping("/search/book/{bookName}")
    public BookResponse getBookByName(@PathVariable String bookName) {
        return bookService.getBookByBookName(bookName);

    }
}
