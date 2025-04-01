package az.texnoera.library_management_system.service.concrets;

import az.texnoera.library_management_system.entity.Book;
import az.texnoera.library_management_system.entity.BorrowBook;
import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.exception_Handle.BasedExceptions;
import az.texnoera.library_management_system.model.enums.StatusCode;
import az.texnoera.library_management_system.model.mapper.BorrowBookMapper;
import az.texnoera.library_management_system.model.request.BorrowBookRequest;
import az.texnoera.library_management_system.model.response.BorrowBookResponse;
import az.texnoera.library_management_system.model.response.Result;
import az.texnoera.library_management_system.repo.BookRepo;
import az.texnoera.library_management_system.repo.BorrowBookRepo;
import az.texnoera.library_management_system.repo.UserRepo;
import az.texnoera.library_management_system.service.abstracts.BorrowBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowBookServiceImpl implements BorrowBookService {
    private final BorrowBookRepo borrowBookRepo;
    private final UserRepo userRepo;
    private final BookRepo bookRepo;

    @Override
    public Result<BorrowBookResponse> getAllBorrows(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BorrowBook> borrowBooks = borrowBookRepo.findAllBorrowBooks(pageable);
        List<BorrowBookResponse> borrowBookResponses = borrowBooks.stream().
                map(BorrowBookMapper::borrowBookToResponse).toList();
        return new Result<>(borrowBookResponses, page, size, borrowBooks.getTotalPages());
    }

    @Override
    public BorrowBookResponse getBorrowById(Long id) {
        BorrowBook borrowBook = borrowBookRepo.findBorrowBookById(id).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BORROW_NOT_FOUND));
        return BorrowBookMapper.borrowBookToResponse(borrowBook);
    }

    @Transactional
    @Override
    public BorrowBookResponse createBorrow(BorrowBookRequest borrowBookRequest) {
        User user = userRepo.findById(borrowBookRequest.getUserId()).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND));

        Book book = bookRepo.findById(borrowBookRequest.getBookId()).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));

        if (book.getAvialableBooksCount() > 0) {
            BorrowBook borrowBook = new BorrowBook();
            borrowBook.setUser(user);
            borrowBook.setBook(book);
            borrowBookRepo.save(borrowBook);
            book.getBorrowBook().add(borrowBook);
            book.setAvialableBooksCount(book.getAvialableBooksCount() - 1);
            bookRepo.save(book);
            user.getBorrowedBooks().add(borrowBook);
            userRepo.save(user);
            return BorrowBookMapper.borrowBookToResponse(borrowBook);
        } else {
            throw new BasedExceptions(HttpStatus.BAD_REQUEST, StatusCode.BOOK_NOT_AVAILABLE);
        }
    }

    @Transactional
    @Override
    public void deleteBorrowByBorrowId(Long id) {
        BorrowBook borrowBook = borrowBookRepo.findBorrowBookById(id).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BORROW_NOT_FOUND));
        User user = borrowBook.getUser();
        Book book = bookRepo.findBookById(borrowBook.getBook().getId()).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));

        book.setAvialableBooksCount(book.getAvialableBooksCount() + 1);
        bookRepo.save(book);

        // Istifadecinin umumi borcundan bu kitabin borcunu cixir
        if (user.getTotalFineAmount() != null) {
            user.setTotalFineAmount(user.getTotalFineAmount().subtract(borrowBook.getFineAmount()));
        }

        // Kitabi istifadecinin borrow siyahisindan cixarir
        user.getBorrowedBooks().remove(borrowBook);
        userRepo.save(user); // Yeni borc melumatini DB-ye yazir

        // Kitab borcunu tam silirik
        borrowBookRepo.delete(borrowBook);
    }
}
