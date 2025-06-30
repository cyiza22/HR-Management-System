package api.example.hrm_system.Leave;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveDTO {
    private Integer employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status;
}
