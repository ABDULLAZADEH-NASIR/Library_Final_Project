package az.texnoera.library_management_system.controller;

import az.texnoera.library_management_system.model.request.BookCheckoutRequest;
import az.texnoera.library_management_system.model.response.BookCheckoutResponse;
import az.texnoera.library_management_system.model.response.BookResponseWithBookCount;
import az.texnoera.library_management_system.model.response.UserResponseWithBookCheckout;
import az.texnoera.library_management_system.service.concrets.BookCheckoutServiceImpl;
import az.texnoera.library_management_system.service.concrets.BookServiceImpl;
import az.texnoera.library_management_system.service.concrets.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
// Burda yalnız userin yeni istifadəçinin istifade ede biləcəyi APİ-lar var
public class UserController {
    private final BookCheckoutServiceImpl bookCheckoutService;
    private final BookServiceImpl bookService;
    private final UserServiceImpl userService;


    // User öz BookCheckout-un yaradır...Yəni istədiyi booku seçir və öz rezervinə əlavə edir
    @PostMapping("/bookCheckouts/create-add-book")
    public BookCheckoutResponse addBookInCheckout(@RequestBody BookCheckoutRequest bookCheckoutRequest) {
        return bookCheckoutService.createCheckout(bookCheckoutRequest);
    }


    // Book id ilə və book sayı da göstərilməklə göstərilir
    @GetMapping("/book/search-with-count/{id}")
    public BookResponseWithBookCount getBookWithCountById(@PathVariable Long id) {
        return bookService.getBookWithCountById(id);
    }

    // User öz profilinə baxır
    @GetMapping("/me")
    public UserResponseWithBookCheckout getCurrentUser() {
        return userService.getCurrentUser();
    }

    // User öz BookCheckout-dan secdiyi booku əgər fiziki olaraq götürməyibsə silib cixara bilir
    @DeleteMapping("/bookCheckouts/delete-for-user/{bookCheckoutId}")
    public void deleteBookInCheckout(@PathVariable Long bookCheckoutId) {
        bookCheckoutService.deleteCheckoutForUser(bookCheckoutId);
    }
}
