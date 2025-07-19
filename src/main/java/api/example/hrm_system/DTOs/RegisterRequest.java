package api.example.hrm_system.DTOs;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Full name is required")
    @Size(max = 50, message = "Full name cannot exceed 50 characters")
    private String fullName;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(EMPLOYEE|MANAGER)$", message = "Role must be either EMPLOYEE or MANAGER")
    private String role; // Only EMPLOYEE or MANAGER allowed through registration

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 50, message = "Email cannot exceed 50 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, and one number")
    private String password;
}