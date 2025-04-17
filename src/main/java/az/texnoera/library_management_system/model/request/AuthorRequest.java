package az.texnoera.library_management_system.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorRequest {
    @NotBlank(message = "Author name cannot be blank")
    @Pattern(
            regexp = "^[A-ZƏÇŞĞÜÖİ][a-zəçşğüöı]{1,29}$",
            message = "Author name must start with a capital letter," +
                    " contain only letters, and have no spaces"
    )
    private String name;

    @NotBlank(message = "Author surname cannot be blank")
    @Pattern(
            regexp = "^[A-ZƏÇŞĞÜÖİ][a-zəçşğüöı]{1,29}$",
            message = "Author surname must start with a capital letter," +
                    " contain only letters, and have no spaces"
    )
    private String surname;
}
