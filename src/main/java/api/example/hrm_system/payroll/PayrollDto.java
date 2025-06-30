package api.example.hrm_system.payroll;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class PayrollDto {
    @NotBlank
    private Long id;
    @NotBlank
    private String employeeName;
    @NotBlank
    private LocalDate payDate;
    @NotBlank
    private BigDecimal grossAmount;
    @NotBlank
    private BigDecimal deductions;
    @NotBlank
    private BigDecimal netAmount;
}