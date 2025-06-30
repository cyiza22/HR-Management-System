package api.example.hrm_system.employee;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDashboardDto {
    @NotBlank
    private String employeeId;
    @NotBlank
    private String fullName;
    @NotBlank
    private String department;
    @NotBlank
    private String designation;
    @NotBlank
    private String employeeType;
    @NotBlank
    private String status;
}
