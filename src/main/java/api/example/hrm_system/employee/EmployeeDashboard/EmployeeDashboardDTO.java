package api.example.hrm_system.employee.EmployeeDashboard;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDashboardDTO {

    @NotBlank
    private String employeeId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String fullName;

    @Email
    private String email;

    @Pattern(regexp = "^[0-9]{10,15}$")
    private String mobileNumber;

    private String department;
    private String designation;
    private String employeeType;
    private String status;
}