package az.texnoera.library_management_system.model.mapper;


import az.texnoera.library_management_system.entity.Author;
import az.texnoera.library_management_system.model.request.AuthorRequest;
import az.texnoera.library_management_system.model.response.AuthorResponse;
import az.texnoera.library_management_system.model.response.BookResponseForAuthor;

import java.util.HashSet;
import java.util.stream.Collectors;

public interface AuthorMapper {

    static Author authorRequestToAuthor(AuthorRequest authorRequest) {
        return Author.builder()
                .name(authorRequest.getName())
                .surname(authorRequest.getSurname())
                .books(new HashSet<>())
                .build();
    }

    static AuthorResponse authorToAuthorResponse(Author author) {
        return AuthorResponse.builder()
                .id(author.getId())
                .name(author.getName())
                .surname(author.getSurname())
                .books(author.getBooks().stream().map(book ->
                        BookResponseForAuthor.builder()
                                .id(book.getId())
                                .name(book.getName())
                                .build()).collect(Collectors.toSet()))
                .build();
    }

    static void authorToAuthorResponseUpdate(Author author, AuthorRequest authorRequest) {
        author.setName(authorRequest.getName());
        author.setSurname(authorRequest.getSurname());
    }
}
