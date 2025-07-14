package api.example.hrm_system.passwordReset;

import api.example.hrm_system.DTOs.EmailService;
import api.example.hrm_system.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    @Transactional
    public void createPasswordResetTokenForUser(User user) {
        // Delete any existing tokens for this user
        tokenRepository.deleteByUser_Id(user.getId());

        // Create new token
        PasswordResetToken token = new PasswordResetToken(user);
        tokenRepository.save(token);

        // Send email with reset link
        String resetLink = "http://localhost:8080/reset-password?token=" + token.getToken();
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
    }

    @Transactional(readOnly = true)
    public boolean validatePasswordResetToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.isExpired()) {
            throw new RuntimeException("Token has expired");
        }

        return true;
    }

    @Transactional(readOnly = true)
    public User getUserByToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.isExpired()) {
            throw new RuntimeException("Token has expired");
        }

        return resetToken.getUser();
    }
}