package az.texnoera.library_management_system.config;

import az.texnoera.library_management_system.entity.BorrowBook;
import az.texnoera.library_management_system.entity.User;
import az.texnoera.library_management_system.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender mailSender;
    private final UserRepo userRepo;

    public void sendMailDebtMessage(User user) {
        user.updateTotalDebt(); // Borcu yenileyir
        userRepo.save(user); // Yenilenmis borcu DB yazir

        if (user.getTotalFineAmountAzn().compareTo(BigDecimal.ZERO) > 0) { // Borc varsa email göndər
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Library Debt Notification");

            StringBuilder fineDetails = new StringBuilder("Dear " + user.getName() + " " + user.getSurname() + ",\n\n" +
                    "The following books have been overdue, and fines have been calculated:\n");

            for (BorrowBook borrowBook : user.getBorrowedBooks()) {
                fineDetails.append("- ").append(borrowBook.getBook().getName())
                        .append(": ").append(borrowBook.getFineAmountAZN()).append(" AZN\n");
            }

            fineDetails.append("\nYour total debt amount is: ").append(user.getTotalFineAmountAzn()).append(" AZN.\n\n")
                    .append("To avoid higher fines, please return the books and settle your debt as soon as possible.\n\n")
                    .append("Thank you.\n\n")
                    .append("Library Management System");

            message.setText(fineDetails.toString());
            mailSender.send(message);
        }
    }
}
