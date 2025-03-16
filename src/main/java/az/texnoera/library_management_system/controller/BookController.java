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

    @GetMapping("/{id}")
    public BookResponse getBookById(@PathVariable Long id) {
        return bookServiceImpl.getBookById(id);
    }

    @GetMapping("/search")
    public BookResponse getBookByName(@RequestParam String bookName) {
        return bookServiceImpl.getBookByBookName(bookName);

    }

    @PostMapping
    public BookResponse createBookBook( @RequestBody BookRequest bookRequest) {
        return bookServiceImpl.createBook(bookRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookServiceImpl.deleteBookById(id);
    }

    @PutMapping("/{id}")
    public BookResponse updateBook(@PathVariable Long id,
                                   @RequestBody BookRequestForBookUpdate bookRequest) {
        return bookServiceImpl.updateBookById(id,bookRequest);

    }







}
