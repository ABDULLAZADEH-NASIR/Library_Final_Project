package az.texnoera.library_management_system.model.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestForUpdate {
    @Pattern(
            regexp = "^[A-ZƏÇŞĞÜÖİ][a-zəçşğüöı]{1,}$",
            message = "Name must start with a capital letter, contain only letters," +
                    " and be at least 2 characters long without spaces"
    )
    private String name;

    @Pattern(
            regexp = "^[A-ZƏÇŞĞÜÖİ][a-zəçşğüöı]{2,}$",
            message = "Surname must start with a capital letter, contain only letters," +
                    " be at least 3 characters long, and have no spaces"
    )
    private String surname;
}
