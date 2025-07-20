package api.example.hrm_system.attendance;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AttendanceDTO {
    @NotNull(message = "Employee name must not be null")
    private String employeeName;

    @NotNull(message = "Employee occupation must not be null")
    private String designation;

    @NotNull(message = "Employee work type must not be null")
    private Attendance.WorkType workType;

    @NotNull(message = "Date must not be null")
    private LocalDate date;

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Attendance.AttendanceStatus status;
}