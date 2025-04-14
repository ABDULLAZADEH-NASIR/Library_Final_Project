package az.texnoera.library_management_system.controller;

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
// Burda ancaq Adminin istifadə edə biləcəyi APİ-lardı
public class AdminController {
    private final AuthorServiceImpl authorService;
    private final BookCheckoutServiceImpl bookCheckoutService;
    private final BookServiceImpl bookService;
    private final UserServiceImpl userService;

    // Authoru id ilə gətirir
    @GetMapping("/authors/search/{id}")
    public AuthorResponseWithBooks getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    // Authoru id ilə silir
    @DeleteMapping("/authors/delete/{id}")
    public void deleteAuthorById(@PathVariable Long id) {
        authorService.deleteAuthorById(id);
    }

    // Yeni author yaradır
    @PostMapping("/authors/create")
    public AuthorResponseWithBooks addAuthor(@RequestBody AuthorRequest authorRequest) {
        return authorService.createAuthor(authorRequest);
    }

    // Authoru id ilə update edir
    @PutMapping("/authors/update/{id}")
    public AuthorResponseWithBooks updateAuthorById(@PathVariable Long id,
                                                    @RequestBody AuthorRequest authorRequest) {
        return authorService.updateAuthorById(id, authorRequest);

    }

    // Authora id ilə kitabını əlavə edir
    @PostMapping("/authors/{authorId}/add-book/{bookId}")
    public AuthorResponseWithBooks addBookToAuthor(@PathVariable Long authorId,
                                                   @PathVariable Long bookId) {
        return authorService.addBookToAuthor(authorId, bookId);

    }

    // Authorun kitabını id ilə onun adından silir
    @DeleteMapping("/authors/{authorId}/remove-book/{bookId}")
    public AuthorResponseWithBooks removeBookFromAuthor(@PathVariable Long authorId,
                                                        @PathVariable Long bookId) {
        return authorService.removeBookFromAuthor(authorId, bookId);

    }

    // Bütün bookCheckoutları göstərir
    @GetMapping("/bookCheckouts/all")
    public Result<BookCheckoutResponse> getAllBookCheckouts(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        return bookCheckoutService.getAllCheckouts(page, size);
    }

    // BookCheckout id ilə göstərir
    @GetMapping("/bookCheckouts/search/{id}")
    public BookCheckoutResponse getBookCheckoutById(@PathVariable Long id) {
        return bookCheckoutService.getCheckoutById(id);
    }

    // BookCheckoutda olan statusu deyisir.Yeni booku gelib fiziki olaraq goturen zaman.
    @PutMapping("/bookCheckouts/update")
    public BookCheckoutResponse updateBookCheckout(@RequestBody CheckoutRequestForStatus request) {
        return bookCheckoutService.isCollectedBook(request);
    }

    // BookCheckoutu id ilə silir
    @DeleteMapping("/bookCheckouts/delete/{id}")
    public void deleteBookCheckout(@PathVariable Long id) {
        bookCheckoutService.deleteCheckoutByCheckoutId(id);
    }

    // Book id ilə göstərir
    @GetMapping("/books/search-book/{id}")
    public BookResponseWithBookCount getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }


    // Yeni book yaradır
    @PostMapping("/books/create")
    public BookResponseWithBookCount createBook(@RequestBody BookRequest bookRequest) {
        return bookService.createBook(bookRequest);
    }

    // Book id ilə silir
    @DeleteMapping("/books/delete/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

    // Book update edir
    @PutMapping("/books/update/{id}")
    public BookResponseWithBookCount updateBook(@PathVariable Long id,
                                                @RequestBody BookRequestForBookUpdate bookRequest) {
        return bookService.updateBookById(id, bookRequest);
    }

    // Useri id ilə gətirir
    @GetMapping("/users/search/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Useri oz BookCheckoutları ilə id-nə görə göstərir
    @GetMapping("/users/search/with-checkouts/{id}")
    public UserResponseWithBookCheckout getUserCheckoutById(@PathVariable Long id) {
        return userService.getUserWithCheckoutsById(id);
    }

    // Bütün userləri göstərir
    @GetMapping("/users/all")
    public Result<UserResponse> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUsers(page, size);
    }

    // Useri id ilə silir
    @DeleteMapping("/users/delete/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }


    // Useri id ilə update edir
    @PutMapping("/users/update/{id}")
    public UserResponse updateUserById(@PathVariable Long id,
                                       @RequestBody UserRequestForUpdate userRequest) {
        return userService.updateUserById(id, userRequest);
    }

    // Useri FİN ilə göstərir
    @GetMapping("/users/search-by-FIN/{fin}")
    public UserResponseWithBookCheckout getUserByFin(@PathVariable String fin) {
        return userService.getUserByFin(fin);
    }
}
