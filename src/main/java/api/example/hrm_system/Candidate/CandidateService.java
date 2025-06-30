package api.example.hrm_system.Candidate;

import api.example.hrm_system.job.Job;
import api.example.hrm_system.job.JobRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;

    public CandidateService(CandidateRepository candidateRepository, JobRepository jobRepository) {
        this.candidateRepository = candidateRepository;
        this.jobRepository = jobRepository;
    }

    public Candidate create(CandidateDTO dto) {
        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Candidate candidate = new Candidate();
        candidate.setName(dto.getCandidateName());
        candidate.setJob_id(dto.getJobId().intValue());
        candidate.setApplyDate(dto.getApplyDate() != null ? dto.getApplyDate().atStartOfDay() : LocalDateTime.now());
        candidate.setEmail(dto.getEmail());
        candidate.setPhoneNumber(dto.getPhoneNumber());
        candidate.setCv_url(dto.getCv_url());
        candidate.setStatus(Candidate.ApplicationStatus.valueOf(dto.getStatus()));
        candidate.setUpdatedAt(LocalDateTime.now());

        return candidateRepository.save(candidate);
    }

    public List<Candidate> findAll() {
        return candidateRepository.findAll();
    }

    public Candidate findById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
    }

    public List<Candidate> findByName(String name) {
        return candidateRepository.findByName(name);
    }

    public Candidate update(CandidateDTO dto, Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        candidate.setName(dto.getCandidateName());
        candidate.setJob_id(dto.getJobId().intValue());
        candidate.setApplyDate(dto.getApplyDate() != null ? dto.getApplyDate().atStartOfDay() : LocalDateTime.now());
        candidate.setEmail(dto.getEmail());
        candidate.setPhoneNumber(dto.getPhoneNumber());
        candidate.setCv_url(dto.getCv_url());
        candidate.setStatus(Candidate.ApplicationStatus.valueOf(dto.getStatus()));
        candidate.setUpdatedAt(LocalDateTime.now());

        return candidateRepository.save(candidate);
    }

    public void deleteById(Long id) {
        candidateRepository.deleteById(id);
    }
}
