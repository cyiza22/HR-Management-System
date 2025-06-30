package api.example.hrm_system.Notification;

import lombok.Data;

@Data
public class NotificationDTO {
    private String message;
    private Integer employeeId; // recipient's ID
}
