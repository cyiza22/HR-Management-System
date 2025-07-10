package api.example.hrm_system.Auth;

import api.example.hrm_system.Config.JwtUtil;
import api.example.hrm_system.DTOs.*;
import api.example.hrm_system.user.Role;
import api.example.hrm_system.user.User;
import api.example.hrm_system.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final OtpService otpService;

    public User register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : Role.EMPLOYEE);

        String otp = otpService.generateOtp();
        user.setOtp(otp);
        user.setVerified(false);

        User savedUser = userRepository.save(user);
        emailService.sendOtpEmail(user.getEmail(), otp);
        return savedUser;
    }

    public String verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (otpService.verifyOtp(user.getOtp(), otp)) {
            user.setVerified(true);
            user.setOtp(null);
            userRepository.save(user);
            return "OTP verified successfully.";
        }
        return "Invalid OTP.";
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isVerified()) {
            throw new RuntimeException("Please verify your account first.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new LoginResponse("Login successful", token, user.getRole());
    }

    public String sendForgotOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = otpService.generateOtp();
        user.setOtp(otp);
        user.setVerified(false);
        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);
        return "OTP sent to your email for password reset.";
    }

    public String resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!otpService.verifyOtp(user.getOtp(), request.getOtp())) {
            return "Invalid OTP.";
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setOtp(null);
        user.setVerified(true);
        userRepository.save(user);
        return "Password reset successfully.";
    }

    public String verifyOtpAndSetPassword(VerifyOtpRequest verifyOtpRequest, String newPassword) {
        User user = userRepository.findByEmail(verifyOtpRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!otpService.verifyOtp(user.getOtp(), verifyOtpRequest.getOtp())) {
            return "Invalid OTP.";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setOtp(null);
        user.setVerified(true);
        userRepository.save(user);
        return "Password set successfully.";
    }
}