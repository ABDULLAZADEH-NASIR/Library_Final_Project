package az.texnoera.library_management_system.controllers;

import az.texnoera.library_management_system.model.request.AuthorRequest;
import az.texnoera.library_management_system.model.request.BookRequest;
import az.texnoera.library_management_system.model.request.BookRequestForBookUpdate;
import az.texnoera.library_management_system.model.request.UserRequestForUpdate;
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


    @GetMapping("/search-Author/{id}")
    public AuthorResponse getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @DeleteMapping("/delete-author/{id}")
    public void deleteAuthorById(@PathVariable Long id) {
        authorService.deleteAuthorById(id);
    }

    @PostMapping("/create-author")
    public AuthorResponse addAuthor(@RequestBody AuthorRequest authorRequest) {
        return authorService.createAuthor(authorRequest);
    }

    @PutMapping("/update-author/{id}")
    public AuthorResponse updateAuthorById(@PathVariable Long id,
                                           @RequestBody AuthorRequest authorRequest) {
        return authorService.updateAuthorById(id, authorRequest);

    }

    @PostMapping("/author/{authorId}/add-book/{bookId}")
    public AuthorResponse addBookToAuthor(@PathVariable Long authorId,
                                          @PathVariable Long bookId) {

        return authorService.addBookToAuthor(authorId, bookId);

    }

    @DeleteMapping("/author/{authorId}/remove-book/{bookId}")
    public AuthorResponse removeBookFromAuthor(@PathVariable Long authorId,
                                               @PathVariable Long bookId) {

        return authorService.removeBookFromAuthor(authorId, bookId);

    }

    @GetMapping("/all-book-checkouts")
    public Result<BookCheckoutResponse> getAllBookCheckouts(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        return bookCheckoutService.getAllCheckouts(page, size);
    }

    @GetMapping("/search-checkoutById/{id}")
    public BookCheckoutResponse getBookCheckoutById(@PathVariable Long id) {
        return bookCheckoutService.getCheckoutById(id);
    }

    @DeleteMapping("/delete-checkout/{id}")
    public void deleteBookCheckout(@PathVariable Long id) {
        bookCheckoutService.deleteCheckoutByCheckoutId(id);
    }

    @GetMapping("/search-book/{id}")
    public BookResponse getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping("/create-book")
    public BookResponseWithBookCount createBook(@RequestBody BookRequest bookRequest) {
        return bookService.createBook(bookRequest);
    }

    @DeleteMapping("/delete-book/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

    @PutMapping("/update-book/{id}")
    public BookResponseWithBookCount updateBook(@PathVariable Long id,
                                                @RequestBody BookRequestForBookUpdate bookRequest) {
        return bookService.updateBookById(id, bookRequest);

    }

    @GetMapping("/search/userId/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/search/user-with-checkouts/{id}")
    public UserResponseWithBookCheckout getUserCheckoutById(@PathVariable Long id) {
        return userService.getUserWithCheckoutsById(id);
    }

    @GetMapping("/all-users")
    public Result<UserResponse> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUsers(page, size);
    }

    @DeleteMapping("/delete-user/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }


    @PutMapping("/update-user/{id}")
    public UserResponse updateUserById(@PathVariable Long id,
                                       @RequestBody UserRequestForUpdate userRequest) {
        return userService.updateUserById(id, userRequest);
    }

    @GetMapping("search/user-with-FIN/{fin}")
    public UserResponseWithBookCheckout getUserByFin(@PathVariable String fin) {
        return userService.getUserByFin(fin);
    }
}
