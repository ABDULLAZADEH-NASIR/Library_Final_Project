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
    private String FIN;
    @NotNull
    private String password;
    @NotNull
    private String email;
    private BigDecimal totalDebt = BigDecimal.valueOf(0);

    @OneToMany(mappedBy = "user")
    private Set<BorrowBook> borrowedBooks = new HashSet<>();

    // Hesablama metodu hansiki umumi borc hesablayir
    public BigDecimal calculateTotalDebt() {
        BigDecimal totalDebt = BigDecimal.ZERO;
        for (BorrowBook borrowBook : borrowedBooks) {
            totalDebt = totalDebt.add(borrowBook.getFineAmountAZN());
        }
        return totalDebt;
    }
}
