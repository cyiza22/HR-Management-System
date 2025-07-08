package api.example.hrm_system.job;

import api.example.hrm_system.department.Department;
import api.example.hrm_system.department.DepartmentRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (minSalary == null || maxSalary == null || minSalary.compareTo(maxSalary) > 0) {
            throw new IllegalArgumentException("Invalid salary range");
        }
        return jobRepository.findBySalaryBetween(minSalary, maxSalary).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<JobDTO> searchJobsByTitle(String title) {
        return jobRepository.findByJobTitleContaining(title).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public JobDTO createJob(JobDTO jobDTO) {
        Job job = mapToEntity(jobDTO);
        Job savedJob = jobRepository.save(job);
        return mapToDTO(savedJob);
    }

    @Transactional
    public Optional<JobDTO> updateJob(Long id, @Valid JobDTO jobDetails) {
        return jobRepository.findById(id)
                .map(job -> {
                    job.setJobTitle(jobDetails.getJobTitle());
                    job.setJobDescription(jobDetails.getJobDescription());

                    if (jobDetails.getDepartmentName() != null &&
                            !jobDetails.getDepartmentName().isEmpty()) {
                        Department department = departmentRepository.findByDepartmentName(jobDetails.getDepartmentName())
                                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
                        job.setDepartment(department);
                    }

                    job.setLocation(jobDetails.getLocation());
                    job.setSalary(jobDetails.getSalary());
                    job.setJobType(jobDetails.getJobType());
                    job.setStatus(jobDetails.getStatus());
                    Job updatedJob = jobRepository.save(job);
                    return mapToDTO(updatedJob);
                });
    }

    @Transactional
    public boolean deleteJob(Long id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Job mapToEntity(JobDTO dto) {
        Job job = new Job();
        job.setJobTitle(dto.getJobTitle());
        job.setJobDescription(dto.getJobDescription());
        job.setLocation(dto.getLocation());
        job.setSalary(dto.getSalary());
        job.setJobType(dto.getJobType());
        job.setStatus(dto.getStatus());

        if (dto.getDepartmentName() != null && !dto.getDepartmentName().isEmpty()) {
            Department department = departmentRepository.findByDepartmentName(dto.getDepartmentName())
                    .orElseThrow(() -> new IllegalArgumentException("Department not found"));
            job.setDepartment(department);
        }

        return job;
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

        if (job.getDepartment() != null) {
            dto.setDepartmentName(job.getDepartment().getDepartmentName());
        }

        return dto;
    }
}