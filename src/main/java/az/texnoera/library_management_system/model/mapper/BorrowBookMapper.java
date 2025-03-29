package az.texnoera.library_management_system.model.mapper;

import az.texnoera.library_management_system.entity.BorrowBook;
import az.texnoera.library_management_system.model.response.BorrowBookResponse;

public interface BorrowBookMapper {

    static BorrowBookResponse borrowBookToResponse(BorrowBook borrowBook) {
        return BorrowBookResponse.builder()
                .id(borrowBook.getId())
                .bookId(borrowBook.getBook().getId())
                .bookName(borrowBook.getBook().getName())
                .userId(borrowBook.getUser().getId())
                .userName(borrowBook.getUser().getName())
                .userSurname(borrowBook.getUser().getSurname())
                .FIN(borrowBook.getUser().getFIN())
                .fineAmountAZN(String.valueOf(borrowBook.getFineAmountAZN())) // Fine amountu formatlayırıq
                .borrowDate(String.valueOf(borrowBook.getBorrowDate()))  // Tarixi formatlayırıq
                .returnDate(String.valueOf(borrowBook.getReturnDate()))  // Tarixi formatlayırıq
                .build();
    }
}
