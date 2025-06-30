package api.example.hrm_system.job;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<JobDTO>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long id) {
        return jobService.getJobById(id)
                .map(dto -> ResponseEntity.ok(dto))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-title")
    public ResponseEntity<JobDTO> getJobByTitle(@RequestParam String jobTitle) {
        return jobService.getJobByTitle(jobTitle)
                .map(dto -> ResponseEntity.ok(dto))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-location")
    public ResponseEntity<List<JobDTO>> getJobsByLocation(@RequestParam String location) {
        return ResponseEntity.ok(jobService.getJobsByLocation(location));
    }

    @GetMapping("/by-type")
    public ResponseEntity<List<JobDTO>> getJobsByType(@RequestParam Job.JobType jobType) {
        return ResponseEntity.ok(jobService.getJobsByType(jobType));
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<JobDTO>> getJobsByStatus(@RequestParam Job.JobStatus status) {
        return ResponseEntity.ok(jobService.getJobsByStatus(status));
    }

    @GetMapping("/by-department/{departmentId}")
    public ResponseEntity<List<JobDTO>> getJobsByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(jobService.getJobsByDepartment(departmentId));
    }

    @GetMapping("/by-department-name")
    public ResponseEntity<List<JobDTO>> getJobsByDepartmentName(@RequestParam String departmentName) {
        return ResponseEntity.ok(jobService.getJobsByDepartmentName(departmentName));
    }

    @GetMapping("/by-salary-range")
    public ResponseEntity<List<JobDTO>> getJobsBySalaryRange(
            @RequestParam BigDecimal minSalary,
            @RequestParam BigDecimal maxSalary) {
        return ResponseEntity.ok(jobService.getJobsBySalaryRange(minSalary, maxSalary));
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobDTO>> searchJobsByTitle(@RequestParam String title) {
        return ResponseEntity.ok(jobService.searchJobsByTitle(title));
    }

    @PostMapping("/create")
    public ResponseEntity<JobDTO> createJob(@RequestBody Job job) {
        JobDTO createdJob = jobService.createJob(job);
        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobDTO> updateJob(
            @PathVariable Long id,
            @RequestBody Job jobDetails) {
        return jobService.updateJob(id, jobDetails)
                .map(dto -> ResponseEntity.ok(dto))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        if (jobService.deleteJob(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}