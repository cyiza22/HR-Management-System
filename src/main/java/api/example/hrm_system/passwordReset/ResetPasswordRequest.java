package api.example.hrm_system.passwordReset;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private String email;
    private String token;
    private String newPassword;
    private String confirmPassword;
}