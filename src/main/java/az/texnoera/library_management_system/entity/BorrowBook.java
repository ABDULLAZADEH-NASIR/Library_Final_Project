package az.texnoera.library_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
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

    private BigDecimal fineAmountAZN=BigDecimal.ZERO;

    @NotNull
    private LocalDate borrowDate;
    @NotNull
    private LocalDate returnDate;

    private boolean isReturned = false;

    // Avtomatik olaraq returnDate təyin edilir(yeni yarandigi tarixe esasen)
    @PrePersist
    public void setReturnDateAutomatically() {
        if (this.borrowDate == null) {
            this.borrowDate = LocalDate.now();
        }
        // 7 gün sonrakı tarixi təyin edir
        this.returnDate = this.borrowDate.plusDays(7);
    }

    public void calculateFine() {
        if (!isReturned && LocalDate.now().isAfter(returnDate)) {
            long overdueDays = ChronoUnit.DAYS.between(returnDate, LocalDate.now());
            BigDecimal finePerDay = new BigDecimal("5");  // Gecikmə üçün hər gün 5 manat cərmə
            this.fineAmountAZN = finePerDay.multiply(BigDecimal.valueOf(overdueDays));
        } else {
            this.fineAmountAZN = BigDecimal.ZERO;
        }
    }

    @PostPersist
    @PostUpdate
    public void updateUserTotalDebt() {
        this.calculateFine();  // Cərmə avtomatik hesablanır
        if (fineAmountAZN.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal currentUserDebt = user.getTotalDebt();  // İstifadəçinin hazirkı borcu
            BigDecimal updatedUserDebt = currentUserDebt.add(this.fineAmountAZN);  // Yeni cərmə ilə cəmlə
            user.setTotalDebt(updatedUserDebt);
        }
    }
}
