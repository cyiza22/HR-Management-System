package api.example.hrm_system.attendance;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AttendanceDTO {
    @NotBlank(message = "Employee name must not be null")
    private String employeeName;
    @NotBlank(message = "Employee occupation must not be null")
    private String designation;
    @NotBlank(message = "Employee work type must not be null")
    private Attendance.WorkType workType;
    @NotBlank(message = "Input the time employee checked in")
    private LocalDate date;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Attendance.AttendanceStatus status;
}
