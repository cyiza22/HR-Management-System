package api.example.hrm_system.Leave;

import api.example.hrm_system.employee.Employee;
import api.example.hrm_system.employee.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveService(LeaveRepository leaveRepository, EmployeeRepository employeeRepository) {
        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public LeaveDTO applyLeave(LeaveDTO dto, String employeeEmail) {
        Employee employee = employeeRepository.findByEmail(employeeEmail)
                .orElseThrow(() -> new RuntimeException("Employee not found with email: " + employeeEmail));

        validateLeaveDates(dto.getStartDate(), dto.getEndDate(), employee.getId());

        Leave leave = new Leave();
        leave.setEmployee(employee);
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());
        leave.setStatus(dto.getStatus() != null ? dto.getStatus() : Leave.LeaveStatus.Pending);

        Leave savedLeave = leaveRepository.save(leave);
        return convertToDTO(savedLeave);
    }

    public List<LeaveDTO> getAllLeaves() {
        return leaveRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LeaveDTO getLeaveById(Integer id) {
        return convertToDTO(leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found with ID: " + id)));
    }

    public List<LeaveDTO> getLeavesByEmployeeEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));

        return leaveRepository.findByEmployee_Id(employee.getId()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<LeaveDTO> getPendingLeavesByDepartment(Long departmentId) {
        return leaveRepository.findByStatus(Leave.LeaveStatus.Pending).stream()
                .filter(leave -> leave.getEmployee().getDepartment() != null &&
                        leave.getEmployee().getDepartment().getId().equals(departmentId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LeaveDTO updateLeave(Integer id, LeaveDTO dto) {
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found with ID: " + id));

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

        return convertToDTO(leaveRepository.save(leave));
    }

    @Transactional
    public LeaveDTO approveLeave(Integer id) {
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found with ID: " + id));
        leave.setStatus(Leave.LeaveStatus.Approved);
        leave.setUpdatedAt(LocalDateTime.now());
        return convertToDTO(leaveRepository.save(leave));
    }

    @Transactional
    public LeaveDTO rejectLeave(Integer id, String rejectionReason) {
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found with ID: " + id));
        leave.setStatus(Leave.LeaveStatus.Rejected);
        leave.setReason(leave.getReason() + " (Rejected: " + rejectionReason + ")");
        leave.setUpdatedAt(LocalDateTime.now());
        return convertToDTO(leaveRepository.save(leave));
    }

    @Transactional
    public void deleteLeave(Integer id) {
        if (!leaveRepository.existsById(id)) {
            throw new RuntimeException("Leave not found with ID: " + id);
        }
        leaveRepository.deleteById(id);
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

    private LeaveDTO convertToDTO(Leave leave) {
        LeaveDTO dto = new LeaveDTO();
        dto.setId(leave.getId());
        dto.setEmployeeId(leave.getEmployee().getId());
        dto.setStartDate(leave.getStartDate());
        dto.setEndDate(leave.getEndDate());
        dto.setReason(leave.getReason());
        dto.setStatus(leave.getStatus());
        return dto;
    }
}