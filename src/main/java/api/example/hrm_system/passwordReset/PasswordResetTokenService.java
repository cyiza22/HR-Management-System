package api.example.hrm_system.passwordReset;

import api.example.hrm_system.DTOs.EmailService;
import api.example.hrm_system.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    @Transactional
    public void createPasswordResetTokenForUser(User user) {
        try {
            // Delete any existing tokens for this user
            tokenRepository.deleteByUser_Id(user.getId());
            log.info("Deleted existing password reset tokens for user: {}", user.getEmail());

            // Create new token
            PasswordResetToken token = new PasswordResetToken(user);
            tokenRepository.save(token);
            log.info("Created new password reset token for user: {}", user.getEmail());

            // Send email with token (not a link since no frontend)
            String resetMessage = "Your password reset token is: " + token.getToken() +
                    "\nThis token expires in 24 hours." +
                    "\nUse this token with the password reset API endpoint.";

            emailService.sendPasswordResetEmail(user.getEmail(), resetMessage);
            log.info("Password reset token sent successfully to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to create password reset token for user: {}", user.getEmail(), e);
            throw new RuntimeException("Failed to send password reset token", e);
        }
    }

    @Transactional(readOnly = true)
    public boolean validatePasswordResetToken(String token) {
        try {
            PasswordResetToken resetToken = tokenRepository.findByToken(token)
                    .orElseThrow(() -> new RuntimeException("Invalid token"));

            if (resetToken.isExpired()) {
                log.warn("Expired password reset token used: {}", token);
                throw new RuntimeException("Token has expired");
            }

            log.info("Password reset token validated successfully");
            return true;
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public User getUserByToken(String token) {
        try {
            PasswordResetToken resetToken = tokenRepository.findByToken(token)
                    .orElseThrow(() -> new RuntimeException("Invalid token"));

            if (resetToken.isExpired()) {
                log.warn("Expired password reset token used: {}", token);
                throw new RuntimeException("Token has expired");
            }

            log.info("User retrieved successfully by token");
            return resetToken.getUser();
        } catch (Exception e) {
            log.error("Failed to get user by token: {}", e.getMessage());
            throw e;
        }
    }
}