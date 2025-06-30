package api.example.hrm_system.payroll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayrollDTO {

    private Long id;
    private String employeeName;
    private BigDecimal ctc;
    private BigDecimal salaryPerMonth;
    private BigDecimal deduction;
    private Payroll.PayrollStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



}