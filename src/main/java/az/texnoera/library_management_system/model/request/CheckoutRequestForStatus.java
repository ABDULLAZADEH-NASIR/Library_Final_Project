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
public class CheckoutRequestForStatus {
    @NotNull(message = "BookCheckout ID cannot be null")
    private Long bookCheckoutId;
}
