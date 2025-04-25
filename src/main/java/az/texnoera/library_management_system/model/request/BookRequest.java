package az.texnoera.library_management_system.model.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRequest {

    @NotBlank(message = "Book name cannot be blank")
    @Size(min = 1, max = 100, message = "Book name must be between 1 and 100 characters")
    private String name;

    @Min(value = 0, message = "Year cannot be negative")
    @Max(value = 2025, message = "Year cannot be in the future")
    private int year;

    @Min(value = 1, message = "Pages must be at least 1")
    private int pages;

    @NotBlank(message = "Category cannot be blank")
    @NotNull(message ="Category cannot be null" )
    private String category;

    @NotNull(message = "Total book count cannot be null")
    @Min(value = 1, message = "Total book count must be at least 1")
    private Long totalBookCount;

    @NotNull(message = "Available book count cannot be null")
    @Min(value = 0, message = "Available book count cannot be negative")
    private Long availableBookCount;
}
