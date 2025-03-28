package az.texnoera.library_management_system.model.mapper;

import az.texnoera.library_management_system.entity.Book;
import az.texnoera.library_management_system.entity.BorrowBook;
import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.model.request.BorrowBookRequest;
import az.texnoera.library_management_system.model.response.BorrowBookResponse;

import java.time.LocalDate;

public interface BorrowBookMapper {
    static BorrowBook requestToBorrowBook(BorrowBookRequest borrowBookRequest) {
        return BorrowBook.builder()
                .book(Book.builder().id(borrowBookRequest.getBookId()).build())
                .user(User.builder().id(borrowBookRequest.getUserId()).build())
                .build();
    }

    static BorrowBookResponse borrowBookToResponse(BorrowBook borrowBook) {
        return BorrowBookResponse.builder()
                .id(borrowBook.getId())
                .bookId(borrowBook.getBook().getId())
                .bookName(borrowBook.getBook().getName())
                .userId(borrowBook.getUser().getId())
                .userName(borrowBook.getUser().getName())
                .userSurname(borrowBook.getUser().getSurname())
                .FIN(borrowBook.getUser().getFIN())
                .fineAmountAZN(borrowBook.getFineAmountAZN())
                .borrowDate(LocalDate.from(borrowBook.getBorrowDate()).atStartOfDay())
                .returnDate(LocalDate.from(borrowBook.getReturnDate()).atStartOfDay())
                .build();
    }

}
