package api.example.hrm_system.Leave;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Integer> {
    List<Leave> findByEmployee_Id(Long employeeId);
    List<Leave> findByStatus(Leave.LeaveStatus status);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END " +
            "FROM Leave l WHERE l.employee.id = :employeeId " +
            "AND l.status = 'Approved' " +
            "AND ((l.startDate BETWEEN :startDate AND :endDate) OR " +
            "(l.endDate BETWEEN :startDate AND :endDate) OR " +
            "(l.startDate <= :startDate AND l.endDate >= :endDate))")
    boolean existsByEmployee_IdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            @Param("employeeId") Long employeeId,
            Leave.LeaveStatus approved, @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}