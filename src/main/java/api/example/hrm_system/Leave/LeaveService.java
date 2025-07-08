package api.example.hrm_system.Leave;

import api.example.hrm_system.employee.Employee;
import api.example.hrm_system.employee.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveService(LeaveRepository leaveRepository, EmployeeRepository employeeRepository) {
        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Leave applyLeave(LeaveDTO dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + dto.getEmployeeId()));

        validateLeaveDates(dto.getStartDate(), dto.getEndDate(), dto.getEmployeeId());

        Leave leave = new Leave();
        leave.setEmployee(employee);
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());
        leave.setStatus(dto.getStatus() != null ? dto.getStatus() : Leave.LeaveStatus.Pending);

        return leaveRepository.save(leave);
    }

    private void validateLeaveDates(LocalDate startDate, LocalDate endDate, Long employeeId) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        boolean hasOverlappingLeave = leaveRepository.existsByEmployee_IdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                employeeId,
                Leave.LeaveStatus.Approved,
                endDate,
                startDate
        );

        if (hasOverlappingLeave) {
            throw new IllegalStateException("Employee already has approved leave for this period");
        }
    }

    public List<Leave> getAllLeaves() {
        return leaveRepository.findAll();
    }

    public Leave getLeaveById(Integer id) {
        return leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found with ID: " + id));
    }

    public List<Leave> getLeavesByEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee not found with ID: " + employeeId);
        }
        return leaveRepository.findByEmployee_Id(employeeId);
    }

    // Add this new method to fix the error
    public List<Leave> getLeavesByStatus(Leave.LeaveStatus status) {
        return leaveRepository.findByStatus(status);
    }

    @Transactional
    public Leave updateLeave(Integer id, LeaveDTO dto) {
        Leave leave = getLeaveById(id);

        if (!leave.getEmployee().getId().equals(dto.getEmployeeId())) {
            throw new IllegalArgumentException("Cannot change employee for an existing leave");
        }

        validateLeaveDates(dto.getStartDate(), dto.getEndDate(), dto.getEmployeeId());

        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());
        if (dto.getStatus() != null) {
            leave.setStatus(dto.getStatus());
        }
        leave.setUpdatedAt(LocalDateTime.now());

        return leaveRepository.save(leave);
    }

    @Transactional
    public Leave approveLeave(Integer id) {
        Leave leave = getLeaveById(id);
        leave.setStatus(Leave.LeaveStatus.Approved);
        leave.setUpdatedAt(LocalDateTime.now());
        return leaveRepository.save(leave);
    }

    @Transactional
    public Leave rejectLeave(Integer id, String rejectionReason) {
        Leave leave = getLeaveById(id);
        leave.setStatus(Leave.LeaveStatus.Rejected);
        leave.setReason(leave.getReason() + " (Rejected: " + rejectionReason + ")");
        leave.setUpdatedAt(LocalDateTime.now());
        return leaveRepository.save(leave);
    }

    @Transactional
    public void deleteLeave(Integer id) {
        if (!leaveRepository.existsById(id)) {
            throw new RuntimeException("Leave not found with ID: " + id);
        }
        leaveRepository.deleteById(id);
    }
}