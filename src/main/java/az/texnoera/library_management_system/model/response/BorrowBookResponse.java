package az.texnoera.library_management_system.model.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowBookResponse {
    private Long id;
    private Long bookId;
    private Long userId;
    private String bookName;
    private BigDecimal fineAmountAZN;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private boolean isReturned;
}
