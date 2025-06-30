package api.example.hrm_system.job;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class JobDTO {
    private Long id;
    private String jobTitle;
    private String jobDescription;
    private String departmentName;
    private String location;
    private BigDecimal salary;
    private Job.JobType jobType;
    private Job.JobStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}