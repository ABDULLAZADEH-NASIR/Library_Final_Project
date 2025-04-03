package az.texnoera.library_management_system.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class OtpService {
    private final JavaMailSender mailSender;
    private static final int OTP_VALID_MINUTES = 5;

    // Yaddaşda OTP-leri saxlayir Integer kimi..
    private final Map<String, Integer> otpStore = new HashMap<>();
    private final Map<String, LocalDateTime> otpExpirationStore = new HashMap<>();

    public int generateOtp() {
        Random random = new Random();
        // 4 reqemli OTP kodu (1000-den 9999-a kimi 4 reqemli OTP yaradir)
        int otp = 1000 + random.nextInt(9000);
        return otp;
    }

    public void saveOtp(String email, int otp) {
        otpStore.put(email, otp);
        otpExpirationStore.put(email, LocalDateTime.now().plusMinutes(OTP_VALID_MINUTES));
    }

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

    public void sendOtpEmail(String toEmail, int otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("OTP Verification");
        message.setText("Your OTP code is: " + otp + ". It will expire in " + OTP_VALID_MINUTES + " minutes.");
        mailSender.send(message);
    }
}
