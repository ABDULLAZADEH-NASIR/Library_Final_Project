package az.texnoera.library_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
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

    private BigDecimal fineAmountAZN = BigDecimal.ZERO;

    @NotNull
    @CreationTimestamp
    private LocalDate borrowDate;
    @NotNull
    private LocalDate returnDate;

    private boolean isReturned = false;

    @PrePersist
    public void setReturnDateAutomatically() {
        if (this.returnDate == null) {
            this.returnDate = this.borrowDate.plusDays(10);
        }
    }

    // Cəriməni hesablayan metod
    public void calculateFine() {
        if (!isReturned && LocalDate.now().isAfter(returnDate)) {
            long overdueDays = ChronoUnit.DAYS.between(returnDate, LocalDate.now());
            BigDecimal finePerDay = new BigDecimal("5"); // Gecikmə üçün hər gün 5 manat cərmə hesablayir
            this.fineAmountAZN = finePerDay.multiply(BigDecimal.valueOf(overdueDays));
        } else {
            this.fineAmountAZN = BigDecimal.ZERO;
        }
    }
}
