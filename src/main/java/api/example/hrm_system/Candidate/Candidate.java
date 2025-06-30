package api.example.hrm_system.Candidate;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidate")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String cv_url;
    private int job_id;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private LocalDateTime applyDate;;
    private LocalDateTime updatedAt;



    public enum ApplicationStatus {
        Applied,
        Interviewed,
        Hired,
        Rejected
    }


}
