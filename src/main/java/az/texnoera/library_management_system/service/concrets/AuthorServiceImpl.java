package az.texnoera.library_management_system.service.concrets;

import az.texnoera.library_management_system.entity.Author;
import az.texnoera.library_management_system.entity.Book;
import az.texnoera.library_management_system.exception_Handle.BasedExceptions;
import az.texnoera.library_management_system.model.enums.StatusCode;
import az.texnoera.library_management_system.model.mapper.AuthorMapper;
import az.texnoera.library_management_system.model.request.AuthorRequest;
import az.texnoera.library_management_system.model.response.AuthorResponse;
import az.texnoera.library_management_system.model.response.AuthorResponseWithBooks;
import az.texnoera.library_management_system.model.response.Result;
import az.texnoera.library_management_system.repo.AuthorRepo;
import az.texnoera.library_management_system.repo.BookRepo;
import az.texnoera.library_management_system.service.abstracts.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepo authorRepo;
    private final BookRepo bookRepo;

    // Bütün authorları göstərir
    @Override
    public Result<AuthorResponse> getAllAuthors(int page, int size) {
        log.info("Getting all authors - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Author> authors = authorRepo.findAllAuthors(pageable);

        List<AuthorResponse> authorResponses =
                authors.stream().map(AuthorMapper::authorToAuthorResponse).toList();

        log.info("Found {} authors on page {}", authorResponses.size(), page);
        return new Result<>(authorResponses, page, size, authors.getTotalPages());
    }

    // Authoru id ilə gətirir
    @Override
    public AuthorResponseWithBooks getAuthorById(Long id) {
        log.info("Getting author by ID: {}", id);
        Author author = authorRepo.findByAuthorId(id).orElseThrow(() -> {
            log.warn("Author not found with ID: {}", id);
            return new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.AUTHOR_NOT_FOUND);
        });
        log.info("Author with ID {} found: {}", id, author.getName());
        return AuthorMapper.authorToAuthorResponseWithBooks(author);
    }

    // Authoru adı ilə axtarır
    @Override
    public AuthorResponseWithBooks getAuthorByAuthorName(String name) {
        log.info("Getting author by name: {}", name);
        Author author = authorRepo.findByAuthorName(name).orElseThrow(() -> {
            log.warn("Author not found with name: {}", name);
            return new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.AUTHOR_NOT_FOUND);
        });
        log.info("Author '{}' found with ID: {}", name, author.getId());
        return AuthorMapper.authorToAuthorResponseWithBooks(author);
    }

    // Authoru id ilə silir
    @Override
    public void deleteAuthorById(Long id) {
        log.info("Deleting author with ID: {}", id);
        Author author = authorRepo.findByAuthorId(id).orElseThrow(() -> {
            log.warn("Cannot delete. Author not found with ID: {}", id);
            return new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.AUTHOR_NOT_FOUND);
        });
        authorRepo.delete(author);
        log.info("Author with ID {} deleted successfully", id);
    }

    // Yeni author yaradır
    @Override
    public AuthorResponseWithBooks createAuthor(AuthorRequest authorRequest) {
        log.info("Creating new author: {}", authorRequest.getName());
        Author author = AuthorMapper.authorRequestToAuthor(authorRequest);
        authorRepo.save(author);
        log.info("Author created with ID: {}", author.getId());
        return AuthorMapper.authorToAuthorResponseWithBooks(author);
    }

    // Authoru id ilə update edir
    @Transactional
    @Override
    public AuthorResponseWithBooks updateAuthorById(Long id, AuthorRequest authorRequest) {
        log.info("Updating author with ID: {}", id);
        Author author = authorRepo.findById(id).orElseThrow(() -> {
            log.warn("Cannot update. Author not found with ID: {}", id);
            return new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.AUTHOR_NOT_FOUND);
        });
        AuthorMapper.authorToAuthorResponseUpdate(author, authorRequest);
        authorRepo.save(author);
        log.info("Author with ID {} updated successfully", id);
        return AuthorMapper.authorToAuthorResponseWithBooks(author);
    }

    // Authora id ilə kitabını əlavə edir
    @Transactional
    @Override
    public AuthorResponseWithBooks addBookToAuthor(Long authorId, Long bookId) {
        log.info("Adding book (ID: {}) to author (ID: {})", bookId, authorId);
        Author author = authorRepo.findByAuthorId(authorId).orElseThrow(() -> {
            log.warn("Author not found with ID: {}", authorId);
            return new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.AUTHOR_NOT_FOUND);
        });
        Book book = bookRepo.findBookById(bookId).orElseThrow(() -> {
            log.warn("Book not found with ID: {}", bookId);
            return new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND);
        });

        author.getBooks().add(book);
        book.getAuthors().add(author);
        authorRepo.save(author);
        bookRepo.save(book);
        log.info("Book with ID {} added to author with ID {}", bookId, authorId);
        return AuthorMapper.authorToAuthorResponseWithBooks(author);
    }

    // Authorun kitabını id ilə onun adından silir
    @Transactional
    @Override
    public AuthorResponseWithBooks removeBookFromAuthor(Long authorId, Long bookId) {
        log.info("Removing book (ID: {}) from author (ID: {})", bookId, authorId);
        Author author = authorRepo.findByAuthorId(authorId).orElseThrow(() -> {
            log.warn("Author not found with ID: {}", authorId);
            return new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.AUTHOR_NOT_FOUND);
        });
        Book book = bookRepo.findBookById(bookId).orElseThrow(() -> {
            log.warn("Book not found with ID: {}", bookId);
            return new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND);
        });

        author.getBooks().remove(book);
        book.getAuthors().remove(author);
        authorRepo.save(author);
        bookRepo.save(book);
        log.info("Book with ID {} removed from author with ID {}", bookId, authorId);
        return AuthorMapper.authorToAuthorResponseWithBooks(author);
    }
}