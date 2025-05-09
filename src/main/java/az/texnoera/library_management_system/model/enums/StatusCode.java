package az.texnoera.library_management_system.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
// Bu Frontede istədiyimiz code və message öturməyimiz üçündür
public enum StatusCode {
    USER_NOT_FOUND(1000, "User not found"),
    BOOK_NOT_FOUND(1001, "Book not found"),
    AUTHOR_NOT_FOUND(1002, "Author not found"),
    CHECKOUT_NOT_FOUND(1003, "Checkout not found"),
    CATEGORY_NOT_FOUND(1004, "Category not found"),
    BOOK_NOT_AVAILABLE(1005, "Book not available"),
    CATEGORY_MISSING(1006, "Category missing"),
    EMAIL_OR_PASSWORD_INCORRECT(1007, "Email or password incorrect"),
    CHECKOUT_ALREADY_COLLECTED(1008, "Checkout already collected"),
    UNAUTHORIZED_ACTION(1009, "Unauthorized action"),
    ACCESSDENIEDEXCEPTION(10010, "Access denied"),
    USER_ALREADY_EXISTS(1001, "User already exists");

    private final int code;
    private final String message;
}
