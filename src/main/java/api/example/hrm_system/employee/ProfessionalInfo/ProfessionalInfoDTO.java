package api.example.hrm_system.employee.ProfessionalInfo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalInfoDTO {
    @NotBlank
    private String employeeId;

    @NotBlank
    private String username;

    @Email
    private String email;

    private String employeeType;
    private String department;
    private String designation;

    private Integer workingDays; 

    private LocalDate joiningDate;
    private String officeLocation;
}