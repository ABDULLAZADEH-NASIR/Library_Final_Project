package az.texnoera.library_management_system.service.otp;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final JavaMailSender mailSender;
    private static final int OTP_VALID_MINUTES = 5;

    // Yaddaşda OTP-ləri saxlayır Integer kimi..
    private final Map<String, Integer> otpStore = new HashMap<>();
    private final Map<String, LocalDateTime> otpExpirationStore = new HashMap<>();

    // 4 rəqəmli OTP kod yaradır
    public int generateOtp() {
        Random random = new Random();
        // 4 rəqəmli OTP kodu (1000-dən 9999-a kimi 4 reqemli OTP yaradır)
        int otp = 1000 + random.nextInt(9000);
        return otp;
    }

    // OTP kodu save edir həmçinin map ilə userin emaili və
    // OTP kodun yaranma  vaxtı save edilir.(5 dəqiqə kimi təyin etmişəm)
    public void saveOtp(String email, int otp) {
        otpStore.put(email, otp);
        otpExpirationStore.put(email, LocalDateTime.now().plusMinutes(OTP_VALID_MINUTES));
    }

    // OTP kodun doğru olub olmamağı yoxlanılır
    public boolean validateOtp(String email, int otp) {
        Integer storedOtp = otpStore.get(email);
        LocalDateTime expirationTime = otpExpirationStore.get(email);

        if (storedOtp == null || expirationTime == null || expirationTime.isBefore(LocalDateTime.now())) {
            otpStore.remove(email);
            otpExpirationStore.remove(email);
            return false;
        }

        if (storedOtp.equals(otp)) {
            otpStore.remove(email);
            otpExpirationStore.remove(email);
            return true;
        }
        return false;
    }

    // Emailin duzgun olub olmadığını yoxlamaq üçün OTP kod gönderilir
    public boolean sendOtpEmail(String toEmail, int otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("OTP Verification");
            message.setText(MessageFormat.format("Your OTP code is: {0}. It will expire in {1} minutes.",
                    String.valueOf(otp), OTP_VALID_MINUTES));
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            System.err.println(MessageFormat.format("Failed to send OTP email to " +
                    "{0}: {1}", toEmail, e.getMessage()));
            return false;
        }
    }
}
