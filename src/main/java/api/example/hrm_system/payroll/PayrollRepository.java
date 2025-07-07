package api.example.hrm_system.payroll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    Optional<Payroll> findByEmployeeName(String employeeName);

    List<Payroll> findByStatus(Payroll.PayrollStatus status);

    @Query("SELECT p FROM Payroll p WHERE LOWER(p.employeeName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Payroll> findByEmployeeNameContainingIgnoreCase(@Param("name") String name);

    long countByStatus(Payroll.PayrollStatus status);

    @Query("SELECT p FROM Payroll p WHERE p.deduction > 0")
    List<Payroll> findEmployeesWithDeductions();

    @Query("SELECT p FROM Payroll p WHERE p.ctc BETWEEN :minCtc AND :maxCtc")
    List<Payroll> findByCtcBetween(@Param("minCtc") BigDecimal minCtc, @Param("maxCtc") BigDecimal maxCtc);

    @Query("SELECT p FROM Payroll p WHERE p.salaryPerMonth BETWEEN :minSalary AND :maxSalary")
    List<Payroll> findBySalaryPerMonthBetween(@Param("minSalary") BigDecimal minSalary, @Param("maxSalary") BigDecimal maxSalary);

    List<Payroll> findAllByOrderByEmployeeNameAsc();

    List<Payroll> findAllByOrderByCtcDesc();

    List<Payroll> findAllByOrderBySalaryPerMonthDesc();

    @Query("SELECT SUM(p.ctc) FROM Payroll p")
    BigDecimal getTotalCtcSum();

    @Query("SELECT SUM(p.salaryPerMonth) FROM Payroll p")
    BigDecimal getTotalSalarySum();

    @Query("SELECT SUM(p.deduction) FROM Payroll p")
    BigDecimal getTotalDeductionsSum();

    @Query("SELECT AVG(p.ctc) FROM Payroll p")
    BigDecimal getAverageCtc();

    @Query("SELECT AVG(p.salaryPerMonth) FROM Payroll p")
    BigDecimal getAverageSalaryPerMonth();

    @Query("SELECT p FROM Payroll p ORDER BY p.ctc DESC LIMIT :limit")
    List<Payroll> findTopEmployeesByCtc(@Param("limit") int limit);

    @Query("SELECT p FROM Payroll p WHERE p.deduction = 0 OR p.deduction IS NULL")
    List<Payroll> findEmployeesWithoutDeductions();

    boolean existsByEmployeeName(String employeeName);

    @Query("SELECT p FROM Payroll p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    List<Payroll> findByCreatedAtBetween(@Param("startDate") java.time.LocalDateTime startDate,
                                         @Param("endDate") java.time.LocalDateTime endDate);
}