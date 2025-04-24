package az.texnoera.library_management_system.service.concrets;

import az.texnoera.library_management_system.entity.Book;
import az.texnoera.library_management_system.entity.BookCheckout;
import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.exception.ApiException;
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
import az.texnoera.library_management_system.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookCheckoutServiceImpl implements BookCheckoutService {
    private final BookCheckoutRepo bookCheckoutRepo;
    private final UserRepo userRepo;
    private final BookRepo bookRepo;
    private final NotificationService notificationService;

    // Bütün bookCheckoutları göstərir
    @Override
    public Result<BookCheckoutResponse> getAllCheckouts(int page, int size) {
        log.info("Fetching all book checkouts - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<BookCheckout> bookCheckouts = bookCheckoutRepo.findAllBookCheckouts(pageable);
        List<BookCheckoutResponse> borrowBookResponses = bookCheckouts.stream().
                map(BookCheckoutMapper::bookCheckoutToResponse).toList();
        return new Result<>(borrowBookResponses, page, size, bookCheckouts.getTotalPages());
    }

    // BookCheckout id ilə göstərir
    @Override
    public BookCheckoutResponse getCheckoutById(Long id) {
        log.info("Fetching book checkout by ID: {}", id);
        BookCheckout bookCheckout = bookCheckoutRepo.findBookCheckoutById(id).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, StatusCode.CHECKOUT_NOT_FOUND));
        return BookCheckoutMapper.bookCheckoutToResponse(bookCheckout);
    }

    // User öz BookCheckout-un yaradır...Yəni istədiyi booku seçir və öz rezervinə əlavə edir
    @Transactional
    @Override
    public BookCheckoutResponse createCheckout(BookCheckoutRequest bookCheckoutRequest) {
        log.info("Creating new book checkout for user ID: {}, book ID: {}",
                bookCheckoutRequest.getUserId(), bookCheckoutRequest.getBookId());

        User user = userRepo.findById(bookCheckoutRequest.getUserId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, StatusCode.USER_NOT_FOUND));

        Book book = bookRepo.findById(bookCheckoutRequest.getBookId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));

        if (book.getAvialableBooksCount() <= 0) {
            log.warn("Book is not available for checkout - Book ID: {}", book.getId());
            throw new ApiException(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_AVAILABLE);
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

        log.info("Book checkout created successfully - ID: {}", bookCheckout.getId());
        return BookCheckoutMapper.bookCheckoutToResponse(bookCheckout);
    }

    // BookCheckoutu id ilə silir
    @Transactional
    @Override
    public void deleteCheckoutByCheckoutId(Long id) {
        log.info("Deleting book checkout by ID: {}", id);
        BookCheckout bookCheckout = bookCheckoutRepo.findBookCheckoutById(id).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, StatusCode.CHECKOUT_NOT_FOUND));
        User user = bookCheckout.getUser();
        Book book = bookRepo.findBookById(bookCheckout.getBook().getId()).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, StatusCode.BOOK_NOT_FOUND));

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
        log.info("Book checkout deleted successfully - ID: {}", id);
    }

    // BookCheckoutda olan statusu deyisir.Yeni booku gelib fiziki olaraq goturen zaman.
    @Transactional
    @Override
    public BookCheckoutResponse isCollectedBook(CheckoutRequestForStatus request) {
        log.info("Marking book as collected - Checkout ID: {}", request.getBookCheckoutId());
        BookCheckout bookCheckout = bookCheckoutRepo.findBookCheckoutById(request.getBookCheckoutId())
                .orElseThrow(() ->
                        new ApiException(HttpStatus.NOT_FOUND, StatusCode.CHECKOUT_NOT_FOUND));

        bookCheckout.setCollected(true);
        bookCheckoutRepo.save(bookCheckout);

        // Kitab götürüldükdən sonra istifadəçiyə email göndərilir
        notificationService.sendMailCheckoutNotification(bookCheckout);
        log.info("Book marked as collected and notification sent - Checkout ID: {}", request.getBookCheckoutId());

        return BookCheckoutMapper.bookCheckoutToResponse(bookCheckout);
    }

    // User öz BookCheckout-dan secdiyi booku əgər fiziki olaraq götürməyibsə silib cixara bilir
    @Transactional
    @Override
    public void deleteCheckoutForUser(Long bookCheckoutId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        log.info("User with email {} is attempting to delete their checkout - ID: {}", email, bookCheckoutId);

        BookCheckout checkout = bookCheckoutRepo.findById(bookCheckoutId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, StatusCode.CHECKOUT_NOT_FOUND));

        if (!checkout.getUser().getEmail().equals(email)) {
            log.warn("Unauthorized action: User email mismatch");
            throw new ApiException(HttpStatus.FORBIDDEN, StatusCode.UNAUTHORIZED_ACTION);
        }

        if (checkout.isCollected()) {
            log.warn("Checkout already collected. Cannot delete - ID: {}", bookCheckoutId);
            throw new ApiException(HttpStatus.BAD_REQUEST, StatusCode.CHECKOUT_ALREADY_COLLECTED);
        }

        checkout.getBook().setAvialableBooksCount(
                checkout.getBook().getAvialableBooksCount() + 1);

        bookCheckoutRepo.delete(checkout);
        log.info("Checkout deleted successfully by user - ID: {}", bookCheckoutId);
    }

    // User əgər 3 dəqiqə ərzində kitabı fiziki olaraq götürmədiyi
    // halda həmin kitabı rezervi hər dəqiqə silinir
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
            log.info("Removed expired and uncollected checkout - ID: {}", checkout.getId());
        }
    }
}