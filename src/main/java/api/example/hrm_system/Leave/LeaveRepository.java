package api.example.hrm_system.Leave;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Integer> {
    List<Leave> findByEmployee_Id(Long employeeId);
}
