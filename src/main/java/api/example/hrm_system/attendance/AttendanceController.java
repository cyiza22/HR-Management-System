package api.example.hrm_system.attendance;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/my-attendance")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<AttendanceDTO>> getMyAttendance(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(attendanceService.getAttendanceByEmployeeEmail(userDetails.getUsername(), date));
    }

    @PostMapping("/assign")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> assignAttendance(
            @RequestParam Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Attendance.WorkType workType) {
        return ResponseEntity.ok(attendanceService.assignAttendance(employeeId, date, workType));
    }

    @GetMapping("/department")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<AttendanceDTO>> getDepartmentAttendance(
            @RequestParam Long departmentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(attendanceService.getAttendanceByDepartment(departmentId, date));
    }

    @GetMapping
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<List<AttendanceDTO>> getAllAttendances() {
        return ResponseEntity.ok(attendanceService.getAllAttendances());
    }

    @GetMapping("/by-status")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<List<AttendanceDTO>> getByStatus(@RequestParam Attendance.AttendanceStatus status) {
        return ResponseEntity.ok(attendanceService.getByStatus(status));
    }
}