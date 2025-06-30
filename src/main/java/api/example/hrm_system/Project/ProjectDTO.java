package api.example.hrm_system.Project;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectDTO {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // e.g., "Completed", "InProcess", "NotStarted"
    private Integer employeeId; // assignedTo
}
