package az.texnoera.library_management_system.controller;

import az.texnoera.library_management_system.model.request.BookCheckoutRequest;
import az.texnoera.library_management_system.model.response.BookCheckoutResponse;
import az.texnoera.library_management_system.model.response.BookResponseWithBookCount;
import az.texnoera.library_management_system.model.response.UserResponseWithBookCheckout;
import az.texnoera.library_management_system.service.concrets.BookCheckoutServiceImpl;
import az.texnoera.library_management_system.service.concrets.BookServiceImpl;
import az.texnoera.library_management_system.service.concrets.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
// Burda yalnız userin yeni istifadəçinin istifade ede biləcəyi APİ-lar var
public class UserController {

    private final BookCheckoutServiceImpl bookCheckoutService;
    private final BookServiceImpl bookService;
    private final UserServiceImpl userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // User öz BookCheckout-un yaradır...Yəni istədiyi booku seçir və öz rezervinə əlavə edir
    @PostMapping("/bookCheckouts/create-add-book")
    public BookCheckoutResponse addBookInCheckout(@RequestBody BookCheckoutRequest bookCheckoutRequest) {
        logger.info("POST /v1/user/bookCheckouts/create-add-book called with bookId={}, " +
                        "userId={}",
                bookCheckoutRequest.getBookId(),
                bookCheckoutRequest.getUserId());
        return bookCheckoutService.createCheckout(bookCheckoutRequest);
    }

    // User öz profilinə baxır
    @GetMapping("/me")
    public UserResponseWithBookCheckout getCurrentUser() {
        logger.info("GET /v1/user/me called - fetching current user's profile");
        return userService.getCurrentUser();
    }

    // User öz BookCheckout-dan secdiyi booku əgər fiziki olaraq götürməyibsə silib cixara bilir
    @DeleteMapping("/bookCheckouts/delete-for-user/{bookCheckoutId}")
    public void deleteBookInCheckout(@PathVariable Long bookCheckoutId) {
        logger.info("DELETE /v1/user/bookCheckouts/delete-for-user/{} called", bookCheckoutId);
        bookCheckoutService.deleteCheckoutForUser(bookCheckoutId);
    }
}