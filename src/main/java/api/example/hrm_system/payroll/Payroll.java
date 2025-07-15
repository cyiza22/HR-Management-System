package api.example.hrm_system.payroll;

import api.example.hrm_system.employee.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payroll")
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "ctc", nullable = false, precision = 12, scale = 2)
    private BigDecimal ctc;

    @Column(name = "salary_per_month", nullable = false, precision = 12, scale = 2)
    private BigDecimal salaryPerMonth;

    @Column(name = "deduction", precision = 12, scale = 2)
    private BigDecimal deduction = BigDecimal.ZERO;


    @Column(name = "bank_name", length = 100)
    private String bankName;

    @Column(name = "bank_account", length = 50)
    private String bankAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PayrollStatus status = PayrollStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    public BigDecimal getNetSalary() {
        BigDecimal net = salaryPerMonth;

        if (deduction != null) {
            net = net.subtract(deduction);
        }

        return net;
    }

    public enum PayrollStatus {
        PENDING,
        COMPLETED
    }
}