package api.example.hrm_system.payroll;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payroll")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    @Column(name = "ctc", nullable = false, precision = 10, scale = 2)
    private BigDecimal ctc;

    @Column(name = "salary_per_month", nullable = false, precision = 10, scale = 2)
    private BigDecimal salaryPerMonth;

    @Column(name = "deduction", precision = 10, scale = 2)
    private BigDecimal deduction;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PayrollStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum PayrollStatus {
        COMPLETED,
        PENDING
    }
}