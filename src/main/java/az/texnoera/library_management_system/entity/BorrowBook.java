package az.texnoera.library_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    private LocalDate borrowDate;
    @NotNull
    private LocalDate returnDate;




    @PrePersist
    public void setDatesAutomatically() {
        if (this.borrowDate == null) {
            this.borrowDate = LocalDate.now();
        }
        if (this.returnDate == null) {
            this.returnDate = this.borrowDate.plusDays(1);
        }
        if (this.fineAmountAZN == null) {
            this.fineAmountAZN = BigDecimal.valueOf(0.00);
        }
    }


    @PreUpdate
    public void calculateFine() {
        if (LocalDate.now().isAfter(returnDate)) {
            long overdueDays = ChronoUnit.DAYS.between(returnDate, LocalDate.now());
            BigDecimal finePerDay = new BigDecimal("5"); // Hər gün üçün 5 manat cərimə

            // Cəriməni artırır, yalnız gecikmə olduqda
            this.fineAmountAZN = fineAmountAZN.add(finePerDay.multiply(BigDecimal.valueOf(overdueDays)));
        } else {
            // Kitab gecikməyibsə, cərimə sıfır qalır
            this.fineAmountAZN = BigDecimal.ZERO;
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
