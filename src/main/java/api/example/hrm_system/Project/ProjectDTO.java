package api.example.hrm_system.Project;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectDTO {
    private Integer id;

    @NotBlank(message = "Project name is required")
    @Size(max = 100, message = "Project name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "Status is required")
    private Project.ProjectStatus status;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;
    private String description;
    private Long departmentId;
}