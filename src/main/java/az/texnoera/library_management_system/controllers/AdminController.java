package az.texnoera.library_management_system.controllers;

import az.texnoera.library_management_system.model.request.*;
import az.texnoera.library_management_system.model.response.*;
import az.texnoera.library_management_system.service.concrets.AuthorServiceImpl;
import az.texnoera.library_management_system.service.concrets.BookCheckoutServiceImpl;
import az.texnoera.library_management_system.service.concrets.BookServiceImpl;
import az.texnoera.library_management_system.service.concrets.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin")
public class AdminController {
    private final AuthorServiceImpl authorService;
    private final BookCheckoutServiceImpl bookCheckoutService;
    private final BookServiceImpl bookService;
    private final UserServiceImpl userService;


    @GetMapping("/authors/search/{id}")
    public AuthorResponse getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @DeleteMapping("/authors/delete/{id}")
    public void deleteAuthorById(@PathVariable Long id) {
        authorService.deleteAuthorById(id);
    }

    @PostMapping("/authors/create")
    public AuthorResponse addAuthor(@RequestBody AuthorRequest authorRequest) {
        return authorService.createAuthor(authorRequest);
    }

    @PutMapping("/authors/update/{id}")
    public AuthorResponse updateAuthorById(@PathVariable Long id,
                                           @RequestBody AuthorRequest authorRequest) {
        return authorService.updateAuthorById(id, authorRequest);

    }

    @PostMapping("/authors/{authorId}/add-book/{bookId}")
    public AuthorResponse addBookToAuthor(@PathVariable Long authorId,
                                          @PathVariable Long bookId) {

        return authorService.addBookToAuthor(authorId, bookId);

    }

    @DeleteMapping("/authors/{authorId}/remove-book/{bookId}")
    public AuthorResponse removeBookFromAuthor(@PathVariable Long authorId,
                                               @PathVariable Long bookId) {

        return authorService.removeBookFromAuthor(authorId, bookId);

    }

    @GetMapping("/bookCheckouts/all")
    public Result<BookCheckoutResponse> getAllBookCheckouts(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        return bookCheckoutService.getAllCheckouts(page, size);
    }

    @GetMapping("/bookCheckouts/search/{id}")
    public BookCheckoutResponse getBookCheckoutById(@PathVariable Long id) {
        return bookCheckoutService.getCheckoutById(id);
    }

    @PutMapping
    public BookCheckoutResponse updateBookCheckout(@RequestBody CheckoutRequestForStatus request) {
        return bookCheckoutService.isCollectedBook(request);
    }

    @DeleteMapping("/bookCheckouts/delete/{id}")
    public void deleteBookCheckout(@PathVariable Long id) {
        bookCheckoutService.deleteCheckoutByCheckoutId(id);
    }

    @GetMapping("/books/search-book/{id}")
    public BookResponse getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping("/books/create")
    public BookResponseWithBookCount createBook(@RequestBody BookRequest bookRequest) {
        return bookService.createBook(bookRequest);
    }

    @DeleteMapping("/books/delete/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

    @PutMapping("/books/update/{id}")
    public BookResponseWithBookCount updateBook(@PathVariable Long id,
                                                @RequestBody BookRequestForBookUpdate bookRequest) {
        return bookService.updateBookById(id, bookRequest);

    }

    @GetMapping("/users/search/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/users/search/with-checkouts/{id}")
    public UserResponseWithBookCheckout getUserCheckoutById(@PathVariable Long id) {
        return userService.getUserWithCheckoutsById(id);
    }

    @GetMapping("/users/all")
    public Result<UserResponse> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUsers(page, size);
    }

    @DeleteMapping("/users/delete/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }


    @PutMapping("/users/update/{id}")
    public UserResponse updateUserById(@PathVariable Long id,
                                       @RequestBody UserRequestForUpdate userRequest) {
        return userService.updateUserById(id, userRequest);
    }

    @GetMapping("/users/search-by-FIN/{fin}")
    public UserResponseWithBookCheckout getUserByFin(@PathVariable String fin) {
        return userService.getUserByFin(fin);
    }
}
