package az.texnoera.library_management_system.controller;

import az.texnoera.library_management_system.model.request.BookRequest;
import az.texnoera.library_management_system.model.request.BookRequestForBookUpdate;
import az.texnoera.library_management_system.model.response.BookResponse;
import az.texnoera.library_management_system.model.response.Result;
import az.texnoera.library_management_system.service.concrets.BookServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookServiceImpl bookServiceImpl;

    @GetMapping
    public Result<BookResponse> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return bookServiceImpl.getAllBooks(page, size);

    }

    @GetMapping("/search/{id}")
    public BookResponse getBookById(@PathVariable Long id) {
        return bookServiceImpl.getBookById(id);
    }

    @GetMapping("/search/bookName/{bookName}")
    public BookResponse getBookByName(@PathVariable String bookName) {
        return bookServiceImpl.getBookByBookName(bookName);

    }

    @PostMapping("/create")
    public BookResponse createBookBook( @RequestBody BookRequest bookRequest) {
        return bookServiceImpl.createBook(bookRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookServiceImpl.deleteBookById(id);
    }

    @PutMapping("/update/{id}")
    public BookResponse updateBook(@PathVariable Long id,
                                   @RequestBody BookRequestForBookUpdate bookRequest) {
        return bookServiceImpl.updateBookById(id,bookRequest);

    }

    @GetMapping("/search/bookCategory/{category}")
    public Result<BookResponse> getBooksByBookCategory(@PathVariable String category,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return bookServiceImpl.getBooksByBookCategory(category,page,size);
    }







}
