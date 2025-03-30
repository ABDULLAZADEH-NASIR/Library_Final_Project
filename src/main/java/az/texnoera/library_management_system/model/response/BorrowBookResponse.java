package az.texnoera.library_management_system.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    private String fineAmount;
    private String borrowDate;
    private String returnDate;
}
