package az.texnoera.library_management_system.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
@Table(name = "book_checkout")
public class BookCheckout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    private BigDecimal fineAmount;

    private boolean isCollected;

    @CreationTimestamp
    private LocalDateTime checkoutDate;

    private LocalDateTime returnDate;


    @PrePersist
    public void setDatesAutomatically() {
        if (this.checkoutDate == null) {
            this.checkoutDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); // Saniyeni saxlayir, nanosaniyeleri sifirlayir
        }
        if (this.returnDate == null) {
            this.returnDate = this.checkoutDate.plusMinutes(10); // Saniye olduğu kimi qalir
        }
        if (this.fineAmount == null) {
            this.fineAmount = BigDecimal.ZERO;
        }

    }

    @PreUpdate
    public void calculateFine() {
        if (LocalDateTime.now().isAfter(returnDate)) {
            long overdueMinutes = ChronoUnit.MINUTES.between(returnDate, LocalDateTime.now());
            BigDecimal finePerMinute = new BigDecimal("1");

            this.fineAmount = finePerMinute.multiply(BigDecimal.valueOf(overdueMinutes));
        } else {
            this.fineAmount = BigDecimal.ZERO;
        }

        // User-in borcunu yenileyir
        if (this.user != null) {
            this.user.updateTotalDebt(); // User-in umumi borcunu yenileyir
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookCheckout bookCheckout)) return false;
        return Objects.equals(id, bookCheckout.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
