package api.example.hrm_system.DTOs;

import api.example.hrm_system.user.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    @Size(max = 50)
    private String fullName;

    @NotNull
    private Role role;

    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;
}