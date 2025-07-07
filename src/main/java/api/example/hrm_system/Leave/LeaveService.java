package api.example.hrm_system.Leave;

import api.example.hrm_system.employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public LeaveService(LeaveRepository leaveRepository, EmployeeRepository employeeRepository) {
        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
    }

    public Leave applyLeave(LeaveDTO dto) {
        Employee employee = employeeRepository.findById(Long.valueOf(dto.getEmployeeId()))
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Leave leave = new Leave();
        leave.setEmployee(employee);
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());
        leave.setStatus(Leave.LeaveStatus.valueOf(dto.getStatus()));
        leave.setCreatedAt(LocalDateTime.now());
        leave.setUpdatedAt(LocalDateTime.now());

        return leaveRepository.save(leave);
    }

    public List<Leave> getAllLeaves() {
        return leaveRepository.findAll();
    }

    public Leave getLeaveById(Integer id) {
        return leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));
    }

    public List<Leave> getLeavesByEmployee(Long employeeId) {
        return leaveRepository.findByEmployee_Id(employeeId);
    }

    public Leave updateLeave(Integer id, LeaveDTO dto) {
        Leave leave = getLeaveById(id);

        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());
        leave.setStatus(Leave.LeaveStatus.valueOf(dto.getStatus()));
        leave.setUpdatedAt(LocalDateTime.now());

        return leaveRepository.save(leave);
    }

    public void deleteLeave(Integer id) {
        leaveRepository.deleteById(id);
    }
}
