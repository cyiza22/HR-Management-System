package api.example.hrm_system.employee.AccountAccess;

import lombok.Data;

@Data
public class AccountAccessDTO {
    private Long employeeId;
    private String slackId;
    private String githubId;
    private String email;
    private String skypeId;
}