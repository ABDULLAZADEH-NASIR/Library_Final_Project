package az.texnoera.library_management_system.service.abstracts;

import az.texnoera.library_management_system.model.request.AuthorRequest;
import az.texnoera.library_management_system.model.response.AuthorResponse;
import az.texnoera.library_management_system.model.response.AuthorResponseWithBooks;
import az.texnoera.library_management_system.model.response.Result;

public interface AuthorService {
    Result<AuthorResponse> getAllAuthors(int page, int size);

    AuthorResponseWithBooks getAuthorById(Long id);

    AuthorResponseWithBooks getAuthorByAuthorName(String name);

    void deleteAuthorById(Long id);

    AuthorResponseWithBooks createAuthor(AuthorRequest authorRequest);

    AuthorResponseWithBooks updateAuthorById(Long id, AuthorRequest authorRequest);

    AuthorResponseWithBooks addBookToAuthor(Long authorId, Long bookId);

    AuthorResponseWithBooks removeBookFromAuthor(Long authorId, Long bookId);
}
