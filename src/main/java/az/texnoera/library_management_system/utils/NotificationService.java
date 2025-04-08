package az.texnoera.library_management_system.utils;

import az.texnoera.library_management_system.entity.Book;
import az.texnoera.library_management_system.entity.BookCheckout;
import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.repo.BookRepo;
import az.texnoera.library_management_system.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender mailSender;
    private final UserRepo userRepo;

    // Kitab götürülməsi, gecikməsi və cərimə haqqında qısa bildiriş
    public void sendMailCheckoutNotification(User user, Book book) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Book Checkout Notification");

        String messageText = "Dear " + user.getName() + " "
                + user.getSurname() + ",\n\nYou have borrowed the book: "
                + book.getName() + ".\nPlease note: if the book is not picked up within " +
                "4 minutes, the checkout will be canceled.\nIf the book is not returned on time," +
                " a fine of 1 AZN per minute will be applied.\n\nLibrary Management System";

        message.setText(messageText);
        mailSender.send(message);
    }


    public void sendMailDebtMessage(User user) {
        user.updateTotalDebt(); // Borcu yenileyir
        userRepo.save(user); // Yenilenmis borcu DB yazir

        if (user.getTotalFineAmount().compareTo(BigDecimal.ZERO) > 0) { // Borc varsa email göndər
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Library Debt Notification");

            StringBuilder fineDetails = new StringBuilder
                    ("Dear " + user.getName() + " " + user.getSurname() + ",\n\n" +
                    "The following books have been overdue, and fines have been calculated:\n");

            for (BookCheckout checkoutBook : user.getBookCheckouts()) {
                fineDetails.append("- ").append(checkoutBook.getBook().getName())
                        .append(": ").append(checkoutBook.getFineAmount()).append(" AZN\n");
            }

            fineDetails.append("\nYour total debt amount is: ").append(user.getTotalFineAmount()).append(" AZN.\n\n")
                    .append("To avoid higher fines, please return the books and settle" +
                            " your debt as soon as possible.\n\n")
                    .append("Thank you.\n\n")
                    .append("Library Management System");

            message.setText(fineDetails.toString());
            mailSender.send(message);
        }
    }
}
