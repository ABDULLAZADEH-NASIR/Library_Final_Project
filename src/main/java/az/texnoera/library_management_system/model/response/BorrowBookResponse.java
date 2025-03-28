package az.texnoera.library_management_system.model.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowBookResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String userSurname;
    private String FIN;
    private Long bookId;
    private String bookName;
    private BigDecimal fineAmountAZN;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
}
