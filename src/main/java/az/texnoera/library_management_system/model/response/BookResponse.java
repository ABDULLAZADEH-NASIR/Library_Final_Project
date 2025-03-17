package az.texnoera.library_management_system.model.response;

import az.texnoera.library_management_system.entity.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Long id;
    private String name;
    private String bookCategory;
    private Set<AuthorResponse>authors;
    private int year;
    private int pages;
    private Long totalBookCount;
    private Long availableBookCount;
}
