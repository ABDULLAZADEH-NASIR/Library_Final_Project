package az.texnoera.library_management_system.service.abstracts;

import az.texnoera.library_management_system.model.request.AuthorRequest;
import az.texnoera.library_management_system.model.response.AuthorResponse;
import az.texnoera.library_management_system.model.response.Result;

public interface AuthorService {
    Result<AuthorResponse> getAllAuthors(int page, int size);
    AuthorResponse getAuthorById(Long id);
    AuthorResponse getAuthorByAuthorName(String name);
    void deleteAuthorById(Long id);
    AuthorResponse createAuthor(AuthorRequest authorRequest);
    AuthorResponse updateAuthorById(Long id, AuthorRequest authorRequest);
    AuthorResponse addBookToAuthor(Long authorId, Long bookId);
    AuthorResponse removeBookFromAuthor(Long authorId, Long bookId);
}
