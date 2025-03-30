package az.texnoera.library_management_system.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@ToString
@Getter
@Setter
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

    private BigDecimal totalFineAmount;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<BorrowBook> borrowedBooks = new HashSet<>();

    @PrePersist
    @PreUpdate
    public void ensureTotalDebtNotNull() {
        if (this.totalFineAmount == null) {
            this.totalFineAmount = BigDecimal.ZERO;
        }
    }

    // Total borcu hesablayıb `totalDebt`-ə yazırıq
    public void updateTotalDebt() {
        BigDecimal totalDebt = BigDecimal.ZERO;
        for (BorrowBook borrowBook : borrowedBooks) {
            totalDebt = totalDebt.add(borrowBook.getFineAmount());
        }
        this.totalFineAmount = totalDebt;
    }

    @JsonProperty("totalDebt")
    public String getFormattedTotalDebt() {
        return totalFineAmount != null ? String.format("%.2f AZN", totalFineAmount.doubleValue()) : "0.00 AZN";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
