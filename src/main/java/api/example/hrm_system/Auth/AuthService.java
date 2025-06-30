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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final OtpService otpService;

    // Register a new user and send OTP
    public User register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.EMPLOYEE); // default role

        String otp = otpService.generateOtp(); //use OtpService
        user.setOtp(otp);
        user.setVerified(false);

        userRepository.save(user);
        emailService.sendOtpEmail(user.getEmail(), otp);

        return user;
    }

    // Verify the OTP
    public String verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (otpService.verifyOtp(user.getOtp(), otp)) { // use OtpService
            user.setVerified(true);
            user.setOtp(null); // clear OTP after success
            userRepository.save(user);
            return "OTP verified successfully.";
        } else {
            return "Invalid OTP.";
        }
    }

    // Login
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isVerified()) {
            throw new RuntimeException("Please verify your account first.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new LoginResponse(token, "Login successful");
    }

    // Forgot password - send OTP
    public String sendForgotOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = otpService.generateOtp(); //use OtpService
        user.setOtp(otp);
        user.setVerified(false); // temporarily unverified for reset
        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);
        return "OTP sent to your email for password reset.";
    }

    //Reset password using OTP
    public String resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!otpService.verifyOtp(user.getOtp(), request.getOtp())) { // use OtpService
            return "Invalid OTP.";
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setOtp(null); // clear OTP
        user.setVerified(true);
        userRepository.save(user);

        return "Password reset successfully.";
    }
}
