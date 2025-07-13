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

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void generateAndSendOtp(User user) {
        try {
            String otp = generateOtp();
            user.setOtp(otp);
            user.setOtpGeneratedTime(LocalDateTime.now());
            userRepository.save(user);
            emailService.sendOtpEmail(user.getEmail(), otp);
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

            if (user.getOtp() == null || !user.getOtp().equals(providedOtp)) {
                return false;
            }

            if (user.getOtpGeneratedTime().plusMinutes(10).isBefore(LocalDateTime.now())) {
                return false;
            }

            user.setOtp(null);
            user.setOtpGeneratedTime(null);
            user.setVerified(true);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            logger.error("OTP verification failed for email: " + email, e);
            throw new RuntimeException("OTP verification failed", e);
        }
    }

    private String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }
}