package api.example.hrm_system.attendance;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDto {
    @NotBlank
    private Long id;
    @NotBlank
    private String employeeName;
    @NotBlank
    private LocalDate date;
    @NotBlank
    private LocalDateTime checkIn;
    @NotBlank
    private LocalDateTime checkOut;
    @NotBlank
    private String status;

}