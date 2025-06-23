package api.example.hrm_system.job;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class JobDto {
    @NotBlank
    private Long id;
    @NotBlank
    private String jobTitle;
    @NotBlank
    private String jobDescription;
    @NotBlank
    private BigDecimal salary;
}