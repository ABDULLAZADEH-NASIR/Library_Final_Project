package az.texnoera.library_management_system.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
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
public class User implements UserDetails {
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


    @ManyToMany
    private Set<Role> roles;

    @Override
    public String getUsername() {
        return this.email;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<BookCheckout> bookCheckouts = new HashSet<>();


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
        for (BookCheckout bookCheckout : bookCheckouts) {
            totalDebt = totalDebt.add(bookCheckout.getFineAmount());
        }
        this.totalFineAmount = totalDebt;
    }

    @JsonProperty("totalDebt")
    public String getFormattedTotalDebt() {
        return totalFineAmount != null ? String.format("%.2f AZN",
                totalFineAmount.doubleValue()) : "0.00 AZN";
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role ->
                new SimpleGrantedAuthority(role.getName())).toList();
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


