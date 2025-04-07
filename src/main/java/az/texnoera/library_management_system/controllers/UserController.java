package az.texnoera.library_management_system.controllers;

import az.texnoera.library_management_system.model.request.BookCheckoutRequest;
import az.texnoera.library_management_system.model.response.BookCheckoutResponse;
import az.texnoera.library_management_system.model.response.BookResponseWithBookCount;
import az.texnoera.library_management_system.service.concrets.BookCheckoutServiceImpl;
import az.texnoera.library_management_system.service.concrets.BookServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {
    private final BookCheckoutServiceImpl bookCheckoutService;
    private final BookServiceImpl bookService;


    @PostMapping("/create-checkout-add-book")
    public BookCheckoutResponse addBookCheckout(@RequestBody BookCheckoutRequest bookCheckoutRequest) {
        return bookCheckoutService.createCheckout(bookCheckoutRequest);
    }

    @GetMapping("/search-book-with-count/{id}")
    public BookResponseWithBookCount getBookWithCountById(@PathVariable Long id) {
        return bookService.getBookWithCountById(id);
    }
}
