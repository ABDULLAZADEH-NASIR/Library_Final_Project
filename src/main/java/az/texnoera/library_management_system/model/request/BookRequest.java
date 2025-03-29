package az.texnoera.library_management_system.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRequest {
    private String name;
    private int year;
    private int pages;
    private String category;
    private Long totalBookCount;
    private Long availableBookCount;
}
