package api.example.hrm_system.attendance;

import api.example.hrm_system.employee.Employee;
import api.example.hrm_system.employee.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    // Add these new methods to fix the controller errors
    public List<AttendanceDTO> getAttendanceByEmployeeEmail(String email, LocalDate date) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));

        List<Attendance> attendances;
        if (date != null) {
            attendances = attendanceRepository.findByEmployeeIdAndDate(employee.getId(), date);
        } else {
            attendances = attendanceRepository.findByEmployeeId(employee.getId());
        }

        return attendances.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public String assignAttendance(Long employeeId, LocalDate date, Attendance.WorkType workType) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setDate(date);
        attendance.setWorkType(workType);
        attendance.setStatus(Attendance.AttendanceStatus.OnTime);

        attendanceRepository.save(attendance);
        return "Attendance assigned successfully for " + employee.getFullName();
    }

    public List<AttendanceDTO> getAttendanceByDepartment(Long departmentId, LocalDate date) {
        List<Employee> departmentEmployees = employeeRepository.findByDepartmentId(departmentId);

        return departmentEmployees.stream()
                .flatMap(employee -> {
                    if (date != null) {
                        return attendanceRepository.findByEmployeeIdAndDate(employee.getId(), date).stream();
                    } else {
                        return attendanceRepository.findByEmployeeId(employee.getId()).stream();
                    }
                })
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Existing methods remain the same
    public List<AttendanceDTO> getAllAttendances() {
        return attendanceRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public AttendanceDTO mapToDTO(Attendance attendance) {
        Employee emp = attendance.getEmployee();
        AttendanceDTO dto = new AttendanceDTO();
        dto.setEmployeeName(emp.getFirstName() + " " + emp.getLastName());
        dto.setDesignation(emp.getDesignation());
        dto.setWorkType(attendance.getWorkType());
        dto.setDate(attendance.getDate());
        dto.setCheckIn(attendance.getCheckIn());
        dto.setCheckOut(attendance.getCheckOut());
        dto.setStatus(attendance.getStatus());
        return dto;
    }

    public List<AttendanceDTO> getByStatus(Attendance.AttendanceStatus status) {
        return attendanceRepository.findByStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<AttendanceDTO> getByDate(LocalDate date) {
        return attendanceRepository.findByDate(date).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}