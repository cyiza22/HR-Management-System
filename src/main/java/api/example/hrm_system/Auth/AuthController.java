package api.example.hrm_system.Auth;

import api.example.hrm_system.DTOs.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
// Removed @CrossOrigin annotation since CORS is handled globally
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        return authService.verifyOtp(request);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam String email) {
        return authService.resendOtp(email);
    }

    @GetMapping("/status")
    public ResponseEntity<?> getAuthStatus() {
        return ResponseEntity.ok().body(Map.of(
                "message", "Authentication service is running",
                "hrEmail", "hrms.hr@gmail.com",
                "availableRoles", new String[]{"EMPLOYEE", "MANAGER", "HR"}
        ));
    }
}