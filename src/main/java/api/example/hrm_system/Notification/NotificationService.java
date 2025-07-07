package api.example.hrm_system.Notification;

import api.example.hrm_system.employee.Employee;
import api.example.hrm_system.employee.ProfessionalInfo.ProfessionalInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ProfessionalInfoRepository professionalInfoRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, ProfessionalInfoRepository professionalInfoRepository) {
        this.notificationRepository = notificationRepository;
        this.professionalInfoRepository = professionalInfoRepository;
    }

    public Notification createNotification(NotificationDTO dto) {
        Employee employee = professionalInfoRepository.findById(Long.valueOf(dto.getEmployeeId()))
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Notification notification = new Notification();
        notification.setMessage(dto.getMessage());
        notification.setTimestamp(LocalDateTime.now());
        notification.setRecipient(employee);

        return notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public List<Notification> getNotificationsByEmployee(Long employeeId) {
        return notificationRepository.findByRecipientId(employeeId);
    }

    public Notification getNotificationById(Integer id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
    }

    public void deleteNotification(Integer id) {
        notificationRepository.deleteById(id);
    }
}
