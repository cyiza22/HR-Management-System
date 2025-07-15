package api.example.hrm_system.payroll;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayrollDTO {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private String employeeName;

    @NotNull(message = "CTC is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "CTC must be greater than 0")
    private BigDecimal ctc;

    @NotNull(message = "Salary per month is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary per month must be greater than 0")
    private BigDecimal salaryPerMonth;

    @DecimalMin(value = "0.0", message = "Deduction must be non-negative")
    private BigDecimal deduction;

    @NotNull(message = "Status is required")
    private Payroll.PayrollStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public BigDecimal getNetSalary() {
        BigDecimal net = salaryPerMonth != null ? salaryPerMonth : BigDecimal.ZERO;

        if (deduction != null) {
            net = net.subtract(deduction);
        }

        return net;
    }
}