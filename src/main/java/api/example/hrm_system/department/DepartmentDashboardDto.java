package api.example.hrm_system.department;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDashboardDto {
    @NotBlank
    private Long id;
    @NotBlank
    private String departmentName;
    @NotBlank
    private String location;
    @NotBlank
    private String managerName;


}
