package az.texnoera.library_management_system.model.mapper;

import az.texnoera.library_management_system.entity.BookCheckout;
import az.texnoera.library_management_system.model.response.BookCheckoutResponse;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public interface BookCheckoutMapper {

    static BookCheckoutResponse bookCheckoutToResponse(BookCheckout bookCheckout) {
        return BookCheckoutResponse.builder()
                .id(bookCheckout.getId())
                .bookId(bookCheckout.getBook().getId())
                .bookName(bookCheckout.getBook().getName())
                .userId(bookCheckout.getUser().getId())
                .userName(bookCheckout.getUser().getName())
                .userSurname(bookCheckout.getUser().getSurname())
                .FIN(bookCheckout.getUser().getFIN())
                .fineAmount(String.valueOf(bookCheckout.getFineAmount())) // Fine amountu string deyeri kimi goturur
                .isCollected(bookCheckout.isCollected())
                .checkoutDate(formatDate(bookCheckout.getCheckoutDate()))
                .returnDate(formatDate(bookCheckout.getReturnDate()))
                .build();
    }

    private static String formatDate(LocalDateTime dateTime) {
        return dateTime.truncatedTo(ChronoUnit.SECONDS).toString().replace("T", " ");
    }
}
