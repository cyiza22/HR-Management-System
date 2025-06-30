package api.example.hrm_system.department;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DepartmentDTO {
    private Long id;
    private String departmentName;
    private String location;
    private String managerName;
    private Department.WorkType workType;
    private Department.EmploymentStatus employmentStatus;
    private int totalEmployees;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}