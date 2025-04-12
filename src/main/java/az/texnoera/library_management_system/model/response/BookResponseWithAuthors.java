package az.texnoera.library_management_system.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponseWithAuthors {
    private Long id;
    private String name;
    private String bookCategory;
    private int year;
    private int pages;
    private Long availableBookCount;
    private Set<AuthorResponseForBook> authors;
}
