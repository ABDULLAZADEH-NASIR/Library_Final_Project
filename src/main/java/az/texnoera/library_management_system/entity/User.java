package az.texnoera.library_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @NotNull
    private String password;
    @NotNull
    private String email;

    private BigDecimal totalDebt = BigDecimal.ZERO;

    @OneToMany(mappedBy = "user")
    private Set<BorrowBook> borrowedBooks = new HashSet<>();

}
