package api.example.hrm_system.passwordReset;

import api.example.hrm_system.Auth.OtpService;
import api.example.hrm_system.DTOs.*;
import api.example.hrm_system.user.User;
import api.example.hrm_system.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            tokenService.createPasswordResetTokenForUser(user);
            return ResponseEntity.ok(Map.of("message", "Password reset link sent to your email"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        try {
            tokenService.validatePasswordResetToken(token);
            return ResponseEntity.ok(Map.of("message", "Token is valid"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            User user = tokenService.getUserByToken(request.getToken());
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/verify-otp-and-set-password")
    public ResponseEntity<?> verifyOtpAndSetPassword(@RequestBody VerifyOtpAndSetPasswordRequest request) {
        try {
            boolean isValid = otpService.verifyOtp(request.getEmail(), request.getOtp());
            if (!isValid) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired OTP"));
            }

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);

            return ResponseEntity.ok(Map.of("message", "Password set successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}