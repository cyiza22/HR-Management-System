package api.example.hrm_system.job;

import api.example.hrm_system.department.Department;
import api.example.hrm_system.department.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final DepartmentRepository departmentRepository;

    public JobService(JobRepository jobRepository, DepartmentRepository departmentRepository) {
        this.jobRepository = jobRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<JobDTO> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<JobDTO> getJobById(Long id) {
        return jobRepository.findById(id)
                .map(this::mapToDTO);
    }

    public Optional<JobDTO> getJobByTitle(String jobTitle) {
        return jobRepository.findByJobTitle(jobTitle)
                .map(this::mapToDTO);
    }

    public List<JobDTO> getJobsByLocation(String location) {
        return jobRepository.findByLocation(location).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<JobDTO> getJobsByType(Job.JobType jobType) {
        return jobRepository.findByJobType(jobType).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<JobDTO> getJobsByStatus(Job.JobStatus status) {
        return jobRepository.findByStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<JobDTO> getJobsByDepartment(Long departmentId) {
        return jobRepository.findByDepartmentId(departmentId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<JobDTO> getJobsByDepartmentName(String departmentName) {
        return jobRepository.findByDepartmentName(departmentName).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<JobDTO> getJobsBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary) {
        return jobRepository.findBySalaryBetween(minSalary, maxSalary).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<JobDTO> searchJobsByTitle(String title) {
        return jobRepository.findByJobTitleContaining(title).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public JobDTO createJob(Job job) {
        Job savedJob = jobRepository.save(job);
        return mapToDTO(savedJob);
    }

    public Optional<JobDTO> updateJob(Long id, Job jobDetails) {
        return jobRepository.findById(id)
                .map(job -> {
                    job.setJobTitle(jobDetails.getJobTitle());
                    job.setJobDescription(jobDetails.getJobDescription());
                    job.setDepartment(jobDetails.getDepartment());
                    job.setLocation(jobDetails.getLocation());
                    job.setSalary(jobDetails.getSalary());
                    job.setJobType(jobDetails.getJobType());
                    job.setStatus(jobDetails.getStatus());
                    Job updatedJob = jobRepository.save(job);
                    return mapToDTO(updatedJob);
                });
    }

    public boolean deleteJob(Long id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private JobDTO mapToDTO(Job job) {
        JobDTO dto = new JobDTO();
        dto.setId(job.getId());
        dto.setJobTitle(job.getJobTitle());
        dto.setJobDescription(job.getJobDescription());
        dto.setLocation(job.getLocation());
        dto.setSalary(job.getSalary());
        dto.setJobType(job.getJobType());
        dto.setStatus(job.getStatus());
        dto.setCreatedAt(job.getCreatedAt());
        dto.setUpdatedAt(job.getUpdatedAt());

        // Set department name if department exists
        if (job.getDepartment() != null) {
            dto.setDepartmentName(job.getDepartment().getDepartmentName());
        }

        return dto;
    }
}
