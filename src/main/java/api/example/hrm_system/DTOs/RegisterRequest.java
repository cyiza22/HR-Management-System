package api.example.hrm_system.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import api.example.hrm_system.user.Role;

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
    private String password;
}