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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;
    private final EmailService emailService;

    @Transactional
    public ResponseEntity<?> register(RegisterRequest request) {
        try {
            if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Full name is required"));
            }

            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email is required"));
            }

            if (request.getPassword() == null || request.getPassword().length() < 8) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Password must be at least 8 characters long"));
            }
            if (!Role.isValidRole(request.getRole())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid role. Valid roles are: EMPLOYEE, MANAGER, HR"));
            }

            if (userRepository.existsByEmail(request.getEmail().toLowerCase())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email already in use"));
            }

            User user = User.builder()
                    .fullName(request.getFullName().trim())
                    .email(request.getEmail().trim().toLowerCase())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.fromString(request.getRole()))
                    .verified(false)
                    .build();

            try {
                // Generate OTP without saving user first
                String otp = generateOtp();
                user.setOtp(otp);
                user.setOtpGeneratedTime(LocalDateTime.now());

                emailService.sendOtpEmail(user.getEmail(), otp);
                logger.info("OTP sent successfully to: {}", user.getEmail());

                // Only save user if OTP was sent successfully
                User savedUser = userRepository.save(user);
                logger.info("User registered successfully with email: {}", savedUser.getEmail());

                return ResponseEntity.ok()
                        .body(Map.of(
                                "message", "Registration successful! OTP sent to your email. Please verify to complete registration.",
                                "email", savedUser.getEmail()
                        ));

            } catch (Exception e) {
                logger.error("Failed to send OTP during registration for email: " + request.getEmail(), e);
                // Don't save user if OTP fails
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Registration failed: Unable to send verification email. Please try again."));
            }

        } catch (Exception e) {
            logger.error("Registration failed for email: " + request.getEmail(), e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

    // Helper method to generate OTP
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generates a 6-digit OTP
        return String.valueOf(otp);
    }

    public ResponseEntity<?> login(LoginRequest request) {
        try {
            // Validate input
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email is required"));
            }

            if (request.getPassword() == null || request.getPassword().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Password is required"));
            }

            String email = request.getEmail().trim().toLowerCase();
            User user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid email or password"));
            }

            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

            logger.info("User logged in successfully: {}", user.getEmail());

            return ResponseEntity.ok()
                    .body(Map.of(
                            "message", "Login successful",
                            "token", token,
                            "role", user.getRole().name(),
                            "fullName", user.getFullName(),
                            "email", user.getEmail()
                    ));
        } catch (BadCredentialsException e) {
            logger.warn("Invalid credentials for email: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        } catch (Exception e) {
            logger.error("Login failed for email: " + request.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> verifyOtp(VerifyOtpRequest request) {
        try {
            // Validate input
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email is required"));
            }

            if (request.getOtp() == null || request.getOtp().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "OTP is required"));
            }

            String email = request.getEmail().trim().toLowerCase();
            boolean isValid = otpService.verifyOtp(email, request.getOtp().trim());

            if (isValid) {
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

                logger.info("OTP verified successfully for email: {}", user.getEmail());

                return ResponseEntity.ok()
                        .body(Map.of(
                                "message", "OTP verified successfully",
                                "token", token,
                                "role", user.getRole().name(),
                                "fullName", user.getFullName(),
                                "email", user.getEmail()
                        ));
            }

            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid or expired OTP"));
        } catch (Exception e) {
            logger.error("OTP verification failed for email: " + request.getEmail(), e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "OTP verification failed: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> resendOtp(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email is required"));
            }

            String cleanEmail = email.trim().toLowerCase();
            User user = userRepository.findByEmail(cleanEmail).orElse(null);

            if (user == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "User not found"));
            }

            otpService.generateAndSendOtp(user);

            logger.info("OTP resent successfully for email: {}", user.getEmail());

            return ResponseEntity.ok()
                    .body(Map.of("message", "New OTP sent to your email"));
        } catch (Exception e) {
            logger.error("Failed to resend OTP to email: " + email, e);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Failed to resend OTP: " + e.getMessage()));
        }
    }
}