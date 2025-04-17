package az.texnoera.library_management_system.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookCheckoutRequest {
    @NotNull(message = "Book ID cannot be null")
    private Long bookId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;
}
