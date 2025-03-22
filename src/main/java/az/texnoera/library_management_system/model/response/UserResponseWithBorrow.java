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
public class UserResponseWithBorrow {
    private Long id;
    private String name;
    private String surname;
    private String FIN;
    private String email;
    private Set<BorrowBookResponseForUser>borrowedBooks;
}
