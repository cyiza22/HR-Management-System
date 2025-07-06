package api.example.hrm_system.attendance;

import api.example.hrm_system.employee.Employee;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<AttendanceDTO> getAllAttendances() {
        return attendanceRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public AttendanceDTO mapToDTO(Attendance attendance) {
        Employee emp = attendance.getEmployee();
        AttendanceDTO dto = new AttendanceDTO();
        dto.setEmployeeName(emp.getFirstName() +" "+ emp.getLastName());
        dto.setDesignation(emp.getDesignation());
        dto.setWorkType(attendance.getWorkType());
        dto.setDate(attendance.getDate());
        dto.setCheckIn(attendance.getCheckIn());
        dto.setCheckOut(attendance.getCheckOut());
        dto.setStatus(attendance.getStatus());
        return dto;
    }

    public List<AttendanceDTO> getByStatus(Attendance.AttendanceStatus status) {
        return attendanceRepository.findByStatus(status).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<AttendanceDTO> getByDate(LocalDate date) {
        return attendanceRepository.findByDate(date).stream().map(this::mapToDTO).collect(Collectors.toList());
    }
}
