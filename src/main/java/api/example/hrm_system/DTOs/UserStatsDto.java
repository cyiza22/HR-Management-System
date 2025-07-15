package api.example.hrm_system.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsDto {
    private long totalUsers;
    private long verifiedUsers;
    private long unverifiedUsers;
    private long employees;
    private long managers;
    private long hrUsers;
}