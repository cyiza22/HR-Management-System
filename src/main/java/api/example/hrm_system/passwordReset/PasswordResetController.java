package api.example.hrm_system.passwordReset;

import api.example.hrm_system.Auth.OtpService;
import api.example.hrm_system.DTOs.*;
import api.example.hrm_system.user.User;
import api.example.hrm_system.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth/password-reset")
@RequiredArgsConstructor
public class PasswordResetController {
    private final PasswordResetTokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;

    @PostMapping("/request")
    public ResponseEntity<?> requestReset(@RequestBody PasswordResetRequest request) {
        try {
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email is required"));
            }

            String email = request.getEmail().trim().toLowerCase();
            User user = userRepository.findByEmail(email)
                    .orElse(null);

            if (user == null) {
                // Don't reveal if email exists or not for security
                return ResponseEntity.ok(Map.of("message", "If the email exists, a password reset link has been sent"));
            }

            tokenService.createPasswordResetTokenForUser(user);
            return ResponseEntity.ok(Map.of("message", "Password reset link sent to your email"));
        } catch (Exception e) {
            log.error("Password reset request failed for email: {}", request.getEmail(), e);
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to process password reset request"));
        }
    }

    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOtpReset(@RequestBody ForgotPasswordRequest request) {
        try {
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email is required"));
            }

            String email = request.getEmail().trim().toLowerCase();
            User user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                // Don't reveal if email exists or not for security
                return ResponseEntity.ok(Map.of("message", "If the email exists, an OTP has been sent"));
            }

            otpService.generateAndSendOtp(user);
            return ResponseEntity.ok(Map.of(
                    "message", "OTP sent to your email for password reset",
                    "email", user.getEmail()
            ));
        } catch (Exception e) {
            log.error("OTP password reset request failed for email: {}", request.getEmail(), e);
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to send OTP"));
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Token is required"));
            }

            tokenService.validatePasswordResetToken(token.trim());
            return ResponseEntity.ok(Map.of("message", "Token is valid"));
        } catch (RuntimeException e) {
            log.warn("Invalid password reset token: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            if (request.getToken() == null || request.getToken().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Token is required"));
            }

            if (request.getNewPassword() == null || request.getNewPassword().length() < 8) {
                return ResponseEntity.badRequest().body(Map.of("error", "Password must be at least 8 characters long"));
            }

            User user = tokenService.getUserByToken(request.getToken().trim());
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);

            log.info("Password reset successfully for user: {}", user.getEmail());
            return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
        } catch (RuntimeException e) {
            log.error("Password reset failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/verify-otp-and-set-password")
    public ResponseEntity<?> verifyOtpAndSetPassword(@RequestBody VerifyOtpAndSetPasswordRequest request) {
        try {
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
            }

            if (request.getOtp() == null || request.getOtp().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "OTP is required"));
            }

            if (request.getNewPassword() == null || request.getNewPassword().length() < 8) {
                return ResponseEntity.badRequest().body(Map.of("error", "Password must be at least 8 characters long"));
            }

            String email = request.getEmail().trim().toLowerCase();
            boolean isValid = otpService.verifyOtp(email, request.getOtp().trim());

            if (!isValid) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired OTP"));
            }

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);

            log.info("Password set successfully via OTP for user: {}", user.getEmail());
            return ResponseEntity.ok(Map.of("message", "Password set successfully"));
        } catch (Exception e) {
            log.error("OTP password reset failed for email: {}", request.getEmail(), e);
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to reset password: " + e.getMessage()));
        }
    }
}