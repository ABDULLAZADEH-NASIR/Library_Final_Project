package az.texnoera.library_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    private Long borrowFine;

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
}
