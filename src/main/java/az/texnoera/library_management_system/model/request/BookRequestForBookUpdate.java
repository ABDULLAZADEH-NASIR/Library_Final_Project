package az.texnoera.library_management_system.model.request;

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
public class BookRequestForBookUpdate {
    private String name;
    private int year;
    private int pages;
    private Long totalBooksCount;


}
