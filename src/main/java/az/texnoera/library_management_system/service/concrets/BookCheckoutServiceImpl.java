package az.texnoera.library_management_system.service.concrets;

import az.texnoera.library_management_system.entity.Book;
import az.texnoera.library_management_system.entity.BookCheckout;
import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.exception_Handle.BasedExceptions;
import az.texnoera.library_management_system.model.enums.StatusCode;
import az.texnoera.library_management_system.model.mapper.BookCheckoutMapper;
import az.texnoera.library_management_system.model.request.BookCheckoutRequest;
import az.texnoera.library_management_system.model.response.BookCheckoutResponse;
import az.texnoera.library_management_system.model.response.Result;
import az.texnoera.library_management_system.repo.BookRepo;
import az.texnoera.library_management_system.repo.BookCheckoutRepo;
import az.texnoera.library_management_system.repo.UserRepo;
import az.texnoera.library_management_system.service.abstracts.BookCheckoutService;
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
public class BookCheckoutServiceImpl implements BookCheckoutService {
    private final BookCheckoutRepo bookCheckoutRepo;
    private final UserRepo userRepo;
    private final BookRepo bookRepo;

    @Override
    public Result<BookCheckoutResponse> getAllCheckouts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookCheckout> bookCheckouts = bookCheckoutRepo.findAllBookCheckouts(pageable);
        List<BookCheckoutResponse> borrowBookResponses = bookCheckouts.stream().
                map(BookCheckoutMapper::bookCheckoutToResponse).toList();
        return new Result<>(borrowBookResponses, page, size, bookCheckouts.getTotalPages());
    }

    @Override
    public BookCheckoutResponse getCheckoutById(Long id) {
        BookCheckout bookCheckout = bookCheckoutRepo.findBookCheckoutById(id).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.CHECKOUT_NOT_FOUND));
        return BookCheckoutMapper.bookCheckoutToResponse(bookCheckout);
    }

    @Transactional
    @Override
    public BookCheckoutResponse createCheckout(BookCheckoutRequest borrowBookRequest) {
        User user = userRepo.findById(borrowBookRequest.getUserId()).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND));

        Book book = bookRepo.findById(borrowBookRequest.getBookId()).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));

        if (book.getAvialableBooksCount() > 0) {
            BookCheckout bookCheckout = new BookCheckout();
            bookCheckout.setUser(user);
            bookCheckout.setBook(book);
            bookCheckoutRepo.save(bookCheckout);
            book.getBookCheckouts().add(bookCheckout);
            book.setAvialableBooksCount(book.getAvialableBooksCount() - 1);
            bookRepo.save(book);
            user.getBookCheckouts().add(bookCheckout);
            userRepo.save(user);
            return BookCheckoutMapper.bookCheckoutToResponse(bookCheckout);
        } else {
            throw new BasedExceptions(HttpStatus.BAD_REQUEST, StatusCode.BOOK_NOT_AVAILABLE);
        }
    }

    @Transactional
    @Override
    public void deleteCheckoutByCheckoutId(Long id) {
        BookCheckout bookCheckout = bookCheckoutRepo.findBookCheckoutById(id).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.CHECKOUT_NOT_FOUND));
        User user = bookCheckout.getUser();
        Book book = bookRepo.findBookById(bookCheckout.getBook().getId()).orElseThrow(() ->
                new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));

        book.setAvialableBooksCount(book.getAvialableBooksCount() + 1);
        bookRepo.save(book);

        // Istifadecinin umumi borcundan bu kitabin borcunu cixir
        if (user.getTotalFineAmount() != null) {
            user.setTotalFineAmount(user.getTotalFineAmount().subtract(bookCheckout.getFineAmount()));
        }

        // Kitabi istifadecinin borrow siyahisindan cixarir
        user.getBookCheckouts().remove(bookCheckout);
        userRepo.save(user); // Yeni borc melumatini DB-ye yazir

        // Kitab borcunu tam silirik
        bookCheckoutRepo.delete(bookCheckout);
    }
}
