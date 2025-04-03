package az.texnoera.library_management_system.controller;

import az.texnoera.library_management_system.model.request.BookCheckoutRequest;
import az.texnoera.library_management_system.model.response.BookCheckoutResponse;
import az.texnoera.library_management_system.model.response.Result;
import az.texnoera.library_management_system.service.concrets.BookCheckoutServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/checkouts")
public class BookCheckoutController {
    private final BookCheckoutServiceImpl bookCheckoutService;

    @GetMapping
    public Result<BookCheckoutResponse> getAllBookCheckouts(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        return bookCheckoutService.getAllCheckouts(page, size);
    }

    @GetMapping("/search-checkoutId/{id}")
    public BookCheckoutResponse getBookCheckoutById(@PathVariable Long id) {
        return bookCheckoutService.getCheckoutById(id);
    }

    @PostMapping("/create")
    public BookCheckoutResponse addBookCheckout(@RequestBody BookCheckoutRequest bookCheckoutRequest) {
        return bookCheckoutService.createCheckout(bookCheckoutRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBookCheckout(@PathVariable Long id) {
        bookCheckoutService.deleteCheckoutByCheckoutId(id);
    }

}
