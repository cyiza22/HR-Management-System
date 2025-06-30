package api.example.hrm_system.Document;

import api.example.hrm_system.employee.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {

    private String name;
    private String url; // Cloudinary URL
    private Employee owner;
    private LocalDateTime uploadedAt;
}
