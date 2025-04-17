package az.texnoera.library_management_system.controller;

import az.texnoera.library_management_system.model.request.BookCheckoutRequest;
import az.texnoera.library_management_system.model.response.BookCheckoutResponse;
import az.texnoera.library_management_system.model.response.UserResponseWithBookCheckout;
import az.texnoera.library_management_system.service.concrets.BookCheckoutServiceImpl;
import az.texnoera.library_management_system.service.concrets.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/user")
// Burda yalnız userin yeni istifadəçinin istifade ede biləcəyi APİ-lar var
public class UserController {

    private final BookCheckoutServiceImpl bookCheckoutService;
    private final UserServiceImpl userService;

    // User öz BookCheckout-un yaradır...Yəni istədiyi booku seçir və öz rezervinə əlavə edir
    @PostMapping("/bookCheckouts/create-add-book")
    public BookCheckoutResponse addBookInCheckout(@RequestBody @Valid BookCheckoutRequest bookCheckoutRequest) {
        log.info("POST /v1/user/bookCheckouts/create-add-book called with bookId={}, " +
                        "userId={}",
                bookCheckoutRequest.getBookId(),
                bookCheckoutRequest.getUserId());
        return bookCheckoutService.createCheckout(bookCheckoutRequest);
    }

    // User öz profilinə baxır
    @GetMapping("/me")
    public UserResponseWithBookCheckout getCurrentUser() {
        log.info("GET /v1/user/me called - fetching current user's profile");
        return userService.getCurrentUser();
    }

    // User öz BookCheckout-dan secdiyi booku əgər fiziki olaraq götürməyibsə silib cixara bilir
    @DeleteMapping("/bookCheckouts/delete-for-user/{bookCheckoutId}")
    public void deleteBookInCheckout(@PathVariable Long bookCheckoutId) {
        log.info("DELETE /v1/user/bookCheckouts/delete-for-user/{} called", bookCheckoutId);
        bookCheckoutService.deleteCheckoutForUser(bookCheckoutId);
    }
}