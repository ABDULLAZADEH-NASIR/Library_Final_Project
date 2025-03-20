package az.texnoera.library_management_system.service.abstracts;

import az.texnoera.library_management_system.model.request.BorrowBookRequest;
import az.texnoera.library_management_system.model.response.BorrowBookResponse;
import az.texnoera.library_management_system.model.response.Result;

public interface BorrowBookService {
    Result<BorrowBookResponse> getAllBorrows(int page,int size);

    BorrowBookResponse getBorrowById(Long id);

    BorrowBookResponse createBorrow(BorrowBookRequest borrowBookRequest);

    void deleteBorrowByBorrowId(Long id);
}
