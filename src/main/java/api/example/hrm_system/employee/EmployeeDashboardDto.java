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
    private String id;
    @NotBlank
    private String fullName;
    @NotBlank
    private String department;
    @NotBlank
    private String designation;
    @NotBlank
    private String employeeId;
    @NotBlank
    private String status;
    @NotBlank
    private String employeeType;

}

