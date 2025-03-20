package az.texnoera.library_management_system.service.concrets;

import az.texnoera.library_management_system.entity.BorrowBook;
import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.model.mapper.BorrowBookMapper;
import az.texnoera.library_management_system.model.request.BorrowBookRequest;
import az.texnoera.library_management_system.model.response.BorrowBookResponse;
import az.texnoera.library_management_system.model.response.Result;
import az.texnoera.library_management_system.repo.BorrowBookRepo;
import az.texnoera.library_management_system.repo.UserRepo;
import az.texnoera.library_management_system.service.abstracts.BorrowBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowBookServiceImpl implements BorrowBookService {
    private final BorrowBookRepo borrowBookRepo;
    private final UserRepo userRepo;

    @Override
    public Result<BorrowBookResponse> getAllBorrows(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BorrowBook> borrowBooks = borrowBookRepo.findAll(pageable);
        List<BorrowBookResponse> borrowBookResponses = borrowBooks.stream().
                map(BorrowBookMapper::borrowBookToResponse).toList();
        return new Result<>(borrowBookResponses, page, size, borrowBooks.getTotalPages());
    }

    @Override
    public BorrowBookResponse getBorrowById(Long id) {
        BorrowBook borrowBook = borrowBookRepo.findById(id).orElseThrow(() ->
                new RuntimeException("BorrowBook not found"));
        return BorrowBookMapper.borrowBookToResponse(borrowBook);
    }

    @Override
    public BorrowBookResponse createBorrow(BorrowBookRequest borrowBookRequest) {
        BorrowBook borrowBook = BorrowBookMapper.requestToBorrowBook(borrowBookRequest);
        borrowBookRepo.save(borrowBook);
        return BorrowBookMapper.borrowBookToResponse(borrowBook);
    }

    @Override
    public void deleteBorrowByBorrowId(Long id) {
        BorrowBook borrowBook=borrowBookRepo.findById(id).orElseThrow(()->
                new RuntimeException("BorrowBook not found"));
        borrowBookRepo.delete(borrowBook);
        
    }


}
