package api.example.hrm_system.Holiday;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "holiday")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer holidayId;

    private String name;
    private LocalDate date;
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setName(String name) {
    }
}
