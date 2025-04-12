package az.texnoera.library_management_system.controllers;

import az.texnoera.library_management_system.model.request.BookCheckoutRequest;
import az.texnoera.library_management_system.model.response.BookCheckoutResponse;
import az.texnoera.library_management_system.model.response.BookResponseWithBookCount;
import az.texnoera.library_management_system.model.response.UserResponse;
import az.texnoera.library_management_system.model.response.UserResponseWithBookCheckout;
import az.texnoera.library_management_system.service.concrets.BookCheckoutServiceImpl;
import az.texnoera.library_management_system.service.concrets.BookServiceImpl;
import az.texnoera.library_management_system.service.concrets.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {
    private final BookCheckoutServiceImpl bookCheckoutService;
    private final BookServiceImpl bookService;
    private final UserServiceImpl userService;


    @PostMapping("/bookCheckouts/create-add-book")
    public BookCheckoutResponse addBookInCheckout(@RequestBody BookCheckoutRequest bookCheckoutRequest) {
        return bookCheckoutService.createCheckout(bookCheckoutRequest);
    }


    @GetMapping("/bookCheckouts/search-with-count/{id}")
    public BookResponseWithBookCount getBookWithCountById(@PathVariable Long id) {
        return bookService.getBookWithCountById(id);
    }

    @GetMapping("/me")
    public UserResponseWithBookCheckout getCurrentUser() {
        return userService.getCurrentUser();
    }
}
