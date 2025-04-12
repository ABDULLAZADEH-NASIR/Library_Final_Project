package az.texnoera.library_management_system.service.concrets;

import az.texnoera.library_management_system.entity.Book;
import az.texnoera.library_management_system.entity.BookCheckout;
import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.exception_Handle.BasedExceptions;
import az.texnoera.library_management_system.model.enums.StatusCode;
import az.texnoera.library_management_system.model.mapper.BookCheckoutMapper;
import az.texnoera.library_management_system.model.request.BookCheckoutRequest;
import az.texnoera.library_management_system.model.request.CheckoutRequestForStatus;
import az.texnoera.library_management_system.model.response.BookCheckoutResponse;
import az.texnoera.library_management_system.model.response.Result;
import az.texnoera.library_management_system.repo.BookRepo;
import az.texnoera.library_management_system.repo.BookCheckoutRepo;
import az.texnoera.library_management_system.repo.UserRepo;
import az.texnoera.library_management_system.service.abstracts.BookCheckoutService;
import az.texnoera.library_management_system.utils.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookCheckoutServiceImpl implements BookCheckoutService {
    private final BookCheckoutRepo bookCheckoutRepo;
    private final UserRepo userRepo;
    private final BookRepo bookRepo;
    private final NotificationService notificationService;

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
    public BookCheckoutResponse createCheckout(BookCheckoutRequest bookCheckoutRequest) {
        User user = userRepo.findById(bookCheckoutRequest.getUserId())
                .orElseThrow(() -> new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND));

        Book book = bookRepo.findById(bookCheckoutRequest.getBookId())
                .orElseThrow(() -> new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));

        if (book.getAvialableBooksCount() <= 0) {
            throw new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_AVAILABLE);
        }

        // BookCheckout yaradılır
        BookCheckout bookCheckout = new BookCheckout();
        bookCheckout.setUser(user);
        bookCheckout.setBook(book);
        bookCheckout.setCheckoutDate(LocalDateTime.now());
        bookCheckout.setCollected(false); // Əgər default false deyilsə
        bookCheckoutRepo.save(bookCheckout);

        // Kitab sayı yenilənir
        book.setAvialableBooksCount(book.getAvialableBooksCount() - 1);
        bookRepo.save(book); // yalnız kitab dəyişibsə lazımdır

        return BookCheckoutMapper.bookCheckoutToResponse(bookCheckout);
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

    @Transactional
    @Override
    public BookCheckoutResponse isCollectedBook(CheckoutRequestForStatus request) {
        BookCheckout bookCheckout = bookCheckoutRepo.findBookCheckoutById(request.getBookCheckoutId())
                .orElseThrow(() ->
                        new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.CHECKOUT_NOT_FOUND));

        bookCheckout.setCollected(true);
        bookCheckoutRepo.save(bookCheckout);

        // Kitab götürüldükdən sonra istifadəçiyə email göndərilir
        notificationService.sendMailCheckoutNotification(bookCheckout);

        return BookCheckoutMapper.bookCheckoutToResponse(bookCheckout);
    }

    @Transactional
    @Override
    public void deleteCheckoutForUser(Long bookCheckoutId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        BookCheckout checkout = bookCheckoutRepo.findById(bookCheckoutId)
                .orElseThrow(() -> new BasedExceptions(HttpStatus.NOT_FOUND, StatusCode.CHECKOUT_NOT_FOUND));

        if (!checkout.getUser().getEmail().equals(email)) {
            throw new BasedExceptions(HttpStatus.FORBIDDEN, StatusCode.UNAUTHORIZED_ACTION);
        }

        if (checkout.isCollected()) {
            throw new BasedExceptions(HttpStatus.BAD_REQUEST, StatusCode.CHECKOUT_ALREADY_COLLECTED);
        }

        checkout.getBook().setAvialableBooksCount(
                checkout.getBook().getAvialableBooksCount() + 1);

        bookCheckoutRepo.delete(checkout);
    }

    @Scheduled(fixedRate = 60000) // Hər 1 dəqiqədə bir çalışır
    @Transactional
    public void removeUncollectedCheckoutsAfter3Minutes() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeMinutesAgo = now.minusMinutes(5);

        // 5 dəqiqə əvvəl yaradılmış və hələ götürülməmiş bookcheckout-ları tapırıq
        Set<BookCheckout> expiredCheckouts = bookCheckoutRepo
                .findExpiredUncollectedWithUserAndBook(threeMinutesAgo);

        for (BookCheckout checkout : expiredCheckouts) {
            User user = checkout.getUser();
            Book book = checkout.getBook();

            if (user != null) {
                user.getBookCheckouts().remove(checkout);
            }

            if (book != null) {
                book.setAvialableBooksCount(book.getAvialableBooksCount() + 1);
            }

            bookCheckoutRepo.delete(checkout);
        }
    }
}




