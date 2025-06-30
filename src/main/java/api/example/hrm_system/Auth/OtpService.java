package api.example.hrm_system.Auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    public String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit OTP
    }

    public boolean verifyOtp(String expectedOtp, String providedOtp) {
        return expectedOtp != null && expectedOtp.equals(providedOtp);
    }
}