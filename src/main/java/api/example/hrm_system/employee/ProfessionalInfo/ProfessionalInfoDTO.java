package api.example.hrm_system.employee.ProfessionalInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalInfoDTO {
    private String employeeId;
    private String username;
    private String email;
    private String employeeType;
    private String department;
    private String designation;
    private String workingDays;
    private LocalDate joiningDate;
    private String officeLocation;
}
