package az.texnoera.library_management_system.service.concrets;

import az.texnoera.library_management_system.entity.Author;
import az.texnoera.library_management_system.entity.Book;
import az.texnoera.library_management_system.exception_Handle.BasedExceptions;
import az.texnoera.library_management_system.model.enums.StatusCode;
import az.texnoera.library_management_system.model.mapper.AuthorMapper;
import az.texnoera.library_management_system.model.request.AuthorRequest;
import az.texnoera.library_management_system.model.response.AuthorResponse;
import az.texnoera.library_management_system.model.response.Result;
import az.texnoera.library_management_system.repo.AuthorRepo;
import az.texnoera.library_management_system.repo.BookRepo;
import az.texnoera.library_management_system.service.abstracts.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepo authorRepo;
    private final BookRepo bookRepo;

    @Override
    public Result<AuthorResponse> getAllAuthors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Author> authors = authorRepo.findAllAuthors(pageable);

        List<AuthorResponse> authorResponses =
                authors.stream().map(AuthorMapper::authorToAuthorResponse).toList();
        return new Result<>(authorResponses, page, size, authors.getTotalPages());
    }

    @Override
    public AuthorResponse getAuthorById(Long id) {
        Author author = authorRepo.findByAuthorId(id).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.AUTHOR_NOT_FOUND));
        return AuthorMapper.authorToAuthorResponse(author);
    }

    @Override
    public AuthorResponse getAuthorByAuthorName(String name) {
        Author author = authorRepo.findByAuthorName(name).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.AUTHOR_NOT_FOUND));
        return AuthorMapper.authorToAuthorResponse(author);
    }

    @Override
    public void deleteAuthorById(Long id) {
        Author author = authorRepo.findByAuthorId(id).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.AUTHOR_NOT_FOUND));
        authorRepo.delete(author);
    }

    @Override
    public AuthorResponse createAuthor(AuthorRequest authorRequest) {
        Author author = AuthorMapper.authorRequestToAuthor(authorRequest);
        authorRepo.save(author);
        return AuthorMapper.authorToAuthorResponse(author);
    }

    @Transactional
    @Override
    public AuthorResponse updateAuthorById(Long id, AuthorRequest authorRequest) {
        Author author = authorRepo.findById(id).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.AUTHOR_NOT_FOUND));
        AuthorMapper.authorToAuthorResponseUpdate(author, authorRequest);
        return AuthorMapper.authorToAuthorResponse(authorRepo.save(author));
    }

    @Transactional
    @Override
    public AuthorResponse addBookToAuthor(Long authorId, Long bookId) {
        Author author = authorRepo.findByAuthorId(authorId).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.AUTHOR_NOT_FOUND));
        Book book = bookRepo.findBookById(bookId).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND,StatusCode.BOOK_NOT_FOUND));
        author.getBooks().add(book);
        book.getAuthors().add(author);
        authorRepo.save(author);
        bookRepo.save(book);
        return AuthorMapper.authorToAuthorResponse(author);
    }
    @Transactional
    @Override
    public AuthorResponse removeBookFromAuthor(Long authorId, Long bookId) {
        Author author = authorRepo.findByAuthorId(authorId).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.AUTHOR_NOT_FOUND));
        Book book = bookRepo.findBookById(bookId).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));
        author.getBooks().remove(book);
        book.getAuthors().remove(author);
        authorRepo.save(author);
        bookRepo.save(book);
        return AuthorMapper.authorToAuthorResponse(author);
    }
}
