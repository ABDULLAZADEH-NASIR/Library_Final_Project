package az.texnoera.library_management_system.model.request;

import jakarta.validation.constraints.Email;
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
public class UserRequest {
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

    @Pattern(
            regexp = "^[A-Z0-9]{7}$",
            message = "FIN code must consist of exactly 7 alphanumeric characters (letters must be uppercase," +
                    " no spaces or special characters allowed)"
    )
    private String FIN;

    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{7,}$",
            message = "Password must be at least 7 characters long and contain at least one letter," +
                    " one digit, and one special character"
    )
    private String password;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String mail;
}
