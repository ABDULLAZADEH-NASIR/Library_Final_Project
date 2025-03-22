package az.texnoera.library_management_system.config;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final JavaMailSender mailSender;
    private static final int OTP_VALID_MINUTES = 5;

    // Yaddaşda OTP-lərin saxlanması
    private final Map<String, String> otpStore = new HashMap<>();
    private final Map<String, LocalDateTime> otpExpirationStore = new HashMap<>(); // OTP-nin bitmə vaxtı

    public String generateOtp() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000); // 4 rəqəmli OTP kodu
        return String.valueOf(otp);
    }

    public void saveOtp(String email, String otp) {
        otpStore.put(email, otp);
        otpExpirationStore.put(email, LocalDateTime.now().plusMinutes(OTP_VALID_MINUTES)); // OTP-nin bitmə vaxtı
    }

    public boolean validateOtp(String email, String otp) {
        String storedOtp = otpStore.get(email);
        LocalDateTime expirationTime = otpExpirationStore.get(email);

        // OTP-nin vaxtını və dəyərini yoxlayırıq
        if (storedOtp == null || expirationTime == null || expirationTime.isBefore(LocalDateTime.now())) {
            otpStore.remove(email); // OTP vaxtı bitibsə, silirik
            otpExpirationStore.remove(email); // Bitmə vaxtını da silirik
            return false; // OTP keçərli deyil
        }

        if (storedOtp.equals(otp)) {
            otpStore.remove(email); // OTP doğru olduqda silirik
            otpExpirationStore.remove(email); // Bitmə vaxtını da silirik
            return true;
        }

        return false;
    }

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("OTP Verification");
        message.setText("Your OTP code is: " + otp + ". It will expire in " + OTP_VALID_MINUTES + " minutes.");
        mailSender.send(message);
    }
}
