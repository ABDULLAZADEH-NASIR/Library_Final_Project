package az.texnoera.library_management_system.service.abstracts;

import az.texnoera.library_management_system.model.request.BookCheckoutRequest;
import az.texnoera.library_management_system.model.response.BookCheckoutResponse;
import az.texnoera.library_management_system.model.response.Result;

public interface BookCheckoutService {
    Result<BookCheckoutResponse> getAllCheckouts(int page, int size);

    BookCheckoutResponse getCheckoutById(Long id);

    BookCheckoutResponse createCheckout(BookCheckoutRequest borrowBookRequest);

    void deleteCheckoutByCheckoutId(Long id);
}
