package api.example.hrm_system.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyOtpAndSetPasswordRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String otp;

    @NotBlank
    private String newPassword;
}