package az.texnoera.library_management_system.controller;

import az.texnoera.library_management_system.model.request.*;
import az.texnoera.library_management_system.model.response.*;
import az.texnoera.library_management_system.service.concrets.AuthorServiceImpl;
import az.texnoera.library_management_system.service.concrets.BookCheckoutServiceImpl;
import az.texnoera.library_management_system.service.concrets.BookServiceImpl;
import az.texnoera.library_management_system.service.concrets.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin")
@Slf4j

// Burda ancaq Adminin istifadə edə biləcəyi APİ-lardı
public class AdminController {
    private final AuthorServiceImpl authorService;
    private final BookCheckoutServiceImpl bookCheckoutService;
    private final BookServiceImpl bookService;
    private final UserServiceImpl userService;

    // Authoru id ilə gətirir
    @GetMapping("/authors/search/{id}")
    @ResponseStatus(HttpStatus.OK) // Status 200 OK
    public AuthorResponseWithBooks getAuthorById(@PathVariable Long id) {
        log.info("Admin requested author by ID: {}", id);
        return authorService.getAuthorById(id);
    }

    // Authoru id ilə silir
    @DeleteMapping("/authors/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void deleteAuthorById(@PathVariable Long id) {
        log.warn("Admin is deleting author with ID: {}", id);
        authorService.deleteAuthorById(id);
    }

    // Yeni author yaradır
    @PostMapping("/authors")
    @ResponseStatus(HttpStatus.CREATED) // 201
    public AuthorResponseWithBooks addAuthor(@RequestBody @Valid AuthorRequest authorRequest) {
        log.info("Admin is creating a new author: {}", authorRequest.getName());
        return authorService.createAuthor(authorRequest);
    }

    // Authoru id ilə update edir
    @PutMapping("/authors/{id}")
    @ResponseStatus(HttpStatus.OK) // 200
    public AuthorResponseWithBooks updateAuthorById(@PathVariable Long id,
                                                    @RequestBody @Valid AuthorRequest authorRequest) {
        log.info("Admin is updating author with ID: {}", id);
        return authorService.updateAuthorById(id, authorRequest);
    }

    // Authora id ilə kitabını əlavə edir
    @PostMapping("/authors/{authorId}/add-book/{bookId}")
    @ResponseStatus(HttpStatus.OK) // 200
    public AuthorResponseWithBooks addBookToAuthor(@PathVariable Long authorId,
                                                   @PathVariable Long bookId) {
        log.info("Admin is adding book ID {} to author ID {}", bookId, authorId);
        return authorService.addBookToAuthor(authorId, bookId);
    }

    // Authorun kitabını id ilə onun adından silir
    @DeleteMapping("/authors/{authorId}/remove-book/{bookId}")
    @ResponseStatus(HttpStatus.OK) // 200
    public AuthorResponseWithBooks removeBookFromAuthor(@PathVariable Long authorId,
                                                        @PathVariable Long bookId) {
        log.info("Admin is removing book ID {} from author ID {}", bookId, authorId);
        return authorService.removeBookFromAuthor(authorId, bookId);
    }

    // Bütün bookCheckoutları göstərir
    @GetMapping("/bookCheckouts")
    @ResponseStatus(HttpStatus.OK) // 200
    public Result<BookCheckoutResponse> getAllBookCheckouts(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        log.info("Admin requested all book checkouts (page: {}, size: {})", page, size);
        return bookCheckoutService.getAllCheckouts(page, size);
    }

    // BookCheckout id ilə göstərir
    @GetMapping("/bookCheckouts/search/{id}")
    @ResponseStatus(HttpStatus.OK) // 200
    public BookCheckoutResponse getBookCheckoutById(@PathVariable Long id) {
        log.info("Admin requested book checkout by ID: {}", id);
        return bookCheckoutService.getCheckoutById(id);
    }

    // BookCheckoutda olan statusu deyisir.Yeni booku gelib fiziki olaraq goturen zaman.
    @PutMapping("/bookCheckouts")
    @ResponseStatus(HttpStatus.OK) // 200
    public BookCheckoutResponse updateBookCheckout(@RequestBody @Valid CheckoutRequestForStatus request) {
        log.info("Admin is updating checkout status for bookCheckout ID: {}", request.getBookCheckoutId());
        return bookCheckoutService.isCollectedBook(request);
    }

    // BookCheckoutu id ilə silir
    @DeleteMapping("/bookCheckouts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) //204
    public void deleteBookCheckout(@PathVariable Long id) {
        log.warn("Admin is deleting book checkout with ID: {}", id);
        bookCheckoutService.deleteCheckoutByCheckoutId(id);
    }

    // Book id ilə göstərir
    @GetMapping("/books/search-book/{id}")
    @ResponseStatus(HttpStatus.OK) // 200
    public BookResponseWithBookCount getBookById(@PathVariable Long id) {
        log.info("Admin requested book by ID: {}", id);
        return bookService.getBookById(id);
    }

    // Yeni book yaradır
    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED) // 201
    public BookResponseWithBookCount createBook(@RequestBody @Valid BookRequest bookRequest) {
        log.info("Admin is creating a new book: {}", bookRequest.getName());
        return bookService.createBook(bookRequest);
    }

    // Book id ilə silir
    @DeleteMapping("/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void deleteBookById(@PathVariable Long id) {
        log.warn("Admin is deleting book with ID: {}", id);
        bookService.deleteBookById(id);
    }

    // Book update edir
    @PutMapping("/books/{id}")
    @ResponseStatus(HttpStatus.OK) // 200
    public BookResponseWithBookCount updateBook(@PathVariable Long id,
                                                @RequestBody @Valid BookRequestForBookUpdate bookRequest) {
        log.info("Admin is updating book with ID: {}", id);
        return bookService.updateBookById(id, bookRequest);
    }

    // Useri id ilə gətirir
    @GetMapping("/users/search/{id}")
    @ResponseStatus(HttpStatus.OK) // 200
    public UserResponse getUserById(@PathVariable Long id) {
        log.info("Admin requested user by ID: {}", id);
        return userService.getUserById(id);
    }

    // Useri oz BookCheckoutları ilə id-nə görə göstərir
    @GetMapping("/users/search/with-checkouts/{id}")
    @ResponseStatus(HttpStatus.OK) // 200
    public UserResponseWithBookCheckout getUserCheckoutById(@PathVariable Long id) {
        log.info("Admin requested user with checkouts by ID: {}", id);
        return userService.getUserWithCheckoutsById(id);
    }

    // Bütün userləri göstərir
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK) // 200
    public Result<UserResponse> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        log.info("Admin requested all users (page: {}, size: {})", page, size);
        return userService.getAllUsers(page, size);
    }

    // Useri id ilə silir
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void deleteUserById(@PathVariable Long id) {
        log.warn("Admin is deleting user with ID: {}", id);
        userService.deleteUserById(id);
    }

    // Useri id ilə update edir
    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK) // 200
    public UserResponse updateUserById(@PathVariable Long id,
                                       @RequestBody @Valid UserRequestForUpdate userRequest) {
        log.info("Admin is updating user with ID: {}", id);
        return userService.updateUserById(id, userRequest);
    }

    // Useri FİN ilə göstərir
    @GetMapping("/users/search-by-FIN/{fin}")
    @ResponseStatus(HttpStatus.OK) // 200
    public UserResponseWithBookCheckout getUserByFin(@PathVariable String fin) {
        log.info("Admin requested user by FIN: {}", fin);
        return userService.getUserByFin(fin);
    }


    // Book id ilə və book sayı da göstərilməklə göstərilir
    @GetMapping("/book/search-with-count/{id}")
    @ResponseStatus(HttpStatus.OK) // 200
    public BookResponseWithBookCount getBookWithCountById(@PathVariable Long id) {
        log.info("GET /v1/user/book/search-with-count/{} called", id);
        return bookService.getBookWithCountById(id);
    }
}