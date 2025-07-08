package api.example.hrm_system.job;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class JobDTO {
    private Long id;

    @NotBlank(message = "Job title is required")
    @Size(max = 100, message = "Job title cannot exceed 100 characters")
    private String jobTitle;

    @Size(min = 10, max = 1000, message = "Description must be between 10-1000 characters")
    private String jobDescription;

    private String departmentName;

    @NotBlank(message = "Location is required")
    private String location;

    @DecimalMin(value = "0.0", message = "Salary must be positive")
    private BigDecimal salary;

    @NotNull(message = "Job type is required")
    private Job.JobType jobType;

    @NotNull(message = "Status is required")
    private Job.JobStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}