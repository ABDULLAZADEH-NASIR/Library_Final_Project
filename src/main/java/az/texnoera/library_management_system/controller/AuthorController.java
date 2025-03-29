package az.texnoera.library_management_system.controller;

import az.texnoera.library_management_system.model.request.AuthorRequest;
import az.texnoera.library_management_system.model.response.AuthorResponse;
import az.texnoera.library_management_system.model.response.Result;
import az.texnoera.library_management_system.service.concrets.AuthorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final AuthorServiceImpl authorService;

    @GetMapping
    public Result<AuthorResponse> getAllAuthors(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return authorService.getAllAuthors(page, size);
    }

    @GetMapping("/search/{id}")
    public AuthorResponse getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @GetMapping("/search-by-name/")
    public AuthorResponse getAuthorByName(@RequestParam String name) {
        return authorService.getAuthorByAuthorName(name);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAuthorById(@PathVariable Long id) {
        authorService.deleteAuthorById(id);
    }

    @PostMapping("/create")
    public AuthorResponse addAuthor(@RequestBody AuthorRequest authorRequest) {
        return authorService.createAuthor(authorRequest);
    }

    @PutMapping("/update/{id}")
    public AuthorResponse updateAuthorById(@PathVariable Long id,
                                           @RequestBody AuthorRequest authorRequest) {
        return authorService.updateAuthorById(id, authorRequest);

    }


    @PostMapping("/{authorId}/add-book/{bookId}")
    public AuthorResponse addBookToAuthor(@PathVariable Long authorId,
                                          @PathVariable Long bookId) {

        return authorService.addBookToAuthor(authorId, bookId);

    }

    @DeleteMapping("/{authorId}/remove-book/{bookId}")
    public AuthorResponse removeBookFromAuthor(@PathVariable Long authorId,
                                               @PathVariable Long bookId) {

        return authorService.removeBookFromAuthor(authorId, bookId);

    }
}
