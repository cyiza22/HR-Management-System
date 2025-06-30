package api.example.hrm_system.Candidate;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CandidateDTO {


    @NotNull(message = "Candidate name required")
    private String candidateName;

    @NotNull(message ="Job Applying for needed")
    private Long jobId;

    private LocalDate applyDate;

    @NotNull(message = "Candidate email required")
    private String email;

    @NotNull(message = "Phone number needed")
    private String phoneNumber;

    private String cv_url;

    private String status;

}
