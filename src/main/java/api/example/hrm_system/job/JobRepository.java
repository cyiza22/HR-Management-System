package api.example.hrm_system.job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findByJobTitle(String jobTitle);
    List<Job> findByLocation(String location);
    List<Job> findByJobType(Job.JobType jobType);
    List<Job> findByStatus(Job.JobStatus status);
    List<Job> findByDepartmentId(Long departmentId);
    List<Job> findBySalaryBetween(BigDecimal minSalary, BigDecimal maxSalary);

    @Query("SELECT j FROM Job j WHERE j.jobTitle LIKE %:title%")
    List<Job> findByJobTitleContaining(String title);

    @Query("SELECT j FROM Job j WHERE j.department.departmentName = :departmentName")
    List<Job> findByDepartmentName(String departmentName);
}
