package api.example.hrm_system.Auth;


import api.example.hrm_system.DTOs.*;
import api.example.hrm_system.Config.JwtUtil;
import api.example.hrm_system.user.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;

    @Transactional
    public ResponseEntity<?> register(RegisterRequest request) {
        try {
            // Validate role format
            if (!Role.isValidRole(request.getRole())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid role. Valid roles are: EMPLOYEE, MANAGER, HR"));
            }

            // Check if email exists
            if (userRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email already in use"));
            }

            // Build and save user
            User user = User.builder()
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.fromString(request.getRole()))
                    .verified(false)
                    .build();

            User savedUser = userRepository.save(user);

            // Send OTP in a separate transaction
            otpService.generateAndSendOtp(savedUser);

            return ResponseEntity.ok()
                    .body(Map.of("message", "OTP sent to your email. Please verify to complete registration."));

        } catch (Exception e) {
            logger.error("Registration failed for email: " + request.getEmail(), e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Registration failed. Please try again later."));
        }
    }

    public ResponseEntity<?> login(LoginRequest request) {
        try {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!user.isVerified()) {
                otpService.generateAndSendOtp(user);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Account not verified. New OTP sent to your email."));
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

            return ResponseEntity.ok()
                    .body(Map.of(
                            "message", "Login successful",
                            "token", token,
                            "role", user.getRole().name(),
                            "fullName", user.getFullName()
                    ));
        } catch (Exception e) {
            logger.error("Login failed for email: " + request.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }
    }

    @Transactional
    public ResponseEntity<?> verifyOtp(VerifyOtpRequest request) {
        try {
            boolean isValid = otpService.verifyOtp(request.getEmail(), request.getOtp());

            if (isValid) {
                User user = userRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new RuntimeException("User not found"));

                String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

                return ResponseEntity.ok()
                        .body(Map.of(
                                "message", "OTP verified successfully",
                                "token", token,
                                "role", user.getRole().name(),
                                "fullName", user.getFullName()
                        ));
            }

            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid or expired OTP"));
        } catch (Exception e) {
            logger.error("OTP verification failed for email: " + request.getEmail(), e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "OTP verification failed. Please try again."));
        }
    }

    public ResponseEntity<?> resendOtp(String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            otpService.generateAndSendOtp(user);

            return ResponseEntity.ok()
                    .body(Map.of("message", "New OTP sent to your email"));
        } catch (Exception e) {
            logger.error("Failed to resend OTP to email: " + email, e);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Failed to resend OTP. Please try again."));
        }
    }
}