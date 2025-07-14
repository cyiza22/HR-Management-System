package api.example.hrm_system.Auth;

import api.example.hrm_system.user.User;
import api.example.hrm_system.user.UserRepository;
import api.example.hrm_system.DTOs.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {
    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);
    private static final int OTP_EXPIRY_MINUTES = 10;

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void generateAndSendOtp(User user) {
        try {
            String otp = generateOtp();
            user.setOtp(otp);
            user.setOtpGeneratedTime(LocalDateTime.now());
            userRepository.save(user);

            logger.info("OTP generated for user: {}", user.getEmail());

            // Send email
            emailService.sendOtpEmail(user.getEmail(), otp);

            logger.info("OTP sent successfully to: {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Failed to generate/send OTP for user: " + user.getEmail(), e);
            throw new RuntimeException("Failed to send OTP", e);
        }
    }

    @Transactional
    public boolean verifyOtp(String email, String providedOtp) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (user.getOtp() == null) {
                logger.warn("No OTP found for user: {}", email);
                return false;
            }

            if (!user.getOtp().equals(providedOtp)) {
                logger.warn("Invalid OTP provided for user: {}", email);
                return false;
            }

            if (user.getOtpGeneratedTime() == null) {
                logger.warn("OTP generation time not found for user: {}", email);
                return false;
            }

            if (user.getOtpGeneratedTime().plusMinutes(OTP_EXPIRY_MINUTES).isBefore(LocalDateTime.now())) {
                logger.warn("Expired OTP for user: {}", email);
                return false;
            }

            // Clear OTP and mark as verified
            user.setOtp(null);
            user.setOtpGeneratedTime(null);
            user.setVerified(true);
            userRepository.save(user);

            logger.info("OTP verified successfully for user: {}", email);
            return true;
        } catch (Exception e) {
            logger.error("OTP verification failed for email: " + email, e);
            throw new RuntimeException("OTP verification failed", e);
        }
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generates a 6-digit OTP
        return String.valueOf(otp);
    }
}