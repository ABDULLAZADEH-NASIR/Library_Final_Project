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
public class BookRequestForUpdate {
    @NotBlank(message = "Book name cannot be blank")
    @Size(min = 1, max = 100, message = "Book name must be between 1 and 100 characters")
    private String name;

    @Min(value = 0, message = "Year cannot be negative")
    @Max(value = 2025, message = "Year cannot be in the future")
    private int year;

    @NotNull(message = "Total book count cannot be null")
    @Min(value = 1, message = "Total book count must be at least 1")
    private Long totalBookCount;
}
