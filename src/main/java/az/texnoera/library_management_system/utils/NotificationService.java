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
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender mailSender;
    private final UserRepo userRepo;

    public void sendMailCheckoutNotification(BookCheckout checkout) {
        User user = checkout.getUser();
        Book book = checkout.getBook();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Book Checkout Notification");

        String messageText = ("Dear %s %s,\n\nYou have checked out the book titled \"%s\" on %s." +
                "\nThe due date for returning the book is: %s." +
                "\nIf the book is not returned on time, a fine of 1 AZN will be applied *for every minute of delay*." +
                "\n\nPlease note that returning books on time is important." +
                "\n\nLibrary Management System")
                .formatted(
                        user.getName(),
                        user.getSurname(),
                        book.getName(),
                        checkout.getCheckoutDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                        checkout.getReturnDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                );

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
                    (("Dear %s %s,\n\nThe following books have been overdue," +
                            " and fines have been calculated:\n").formatted(user.getName(),
                            user.getSurname()));

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
