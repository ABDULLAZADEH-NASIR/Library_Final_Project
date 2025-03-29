package az.texnoera.library_management_system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "borrow_book")
public class BorrowBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Book book;

    @ManyToOne
    @JoinColumn
    private User user;

    private BigDecimal fineAmountAZN;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP(0)") // Nanosaniyeleri sifirlayir
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime borrowDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime returnDate;


    @PrePersist
    public void setDatesAutomatically() {
        if (this.borrowDate == null) {
            this.borrowDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); // Saniyeni saxlayir, nanosaniyeleri sifirlayir
        }
        if (this.returnDate == null) {
            this.returnDate = this.borrowDate.plusMinutes(10); // Saniye olduğu kimi qalir
        }
        if (this.fineAmountAZN == null) {
            this.fineAmountAZN = BigDecimal.ZERO;
        }

    }

    @PreUpdate
    public void calculateFine() {
        if (LocalDateTime.now().isAfter(returnDate)) {
            long overdueMinutes = ChronoUnit.MINUTES.between(returnDate, LocalDateTime.now());
            BigDecimal finePerMinute = new BigDecimal("5");

            this.fineAmountAZN = finePerMinute.multiply(BigDecimal.valueOf(overdueMinutes));
        } else {
            this.fineAmountAZN = BigDecimal.ZERO;
        }

        // User-in borcunu yenileyir
        if (this.user != null) {
            this.user.updateTotalDebt(); // User-in umumi borcunu yenileyir
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BorrowBook borrowBook)) return false;
        return Objects.equals(id, borrowBook.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
