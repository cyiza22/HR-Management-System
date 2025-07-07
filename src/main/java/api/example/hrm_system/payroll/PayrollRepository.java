package api.example.hrm_system.payroll;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
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
}
