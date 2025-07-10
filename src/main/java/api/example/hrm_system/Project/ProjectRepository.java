package api.example.hrm_system.Project;

import api.example.hrm_system.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByAssignedTo_Id(Long employeeId);
    List<Project> findByStatus(Project.ProjectStatus status);
//    Optional<Employee> findByEmail(String email);
    List<Project> findByAssignedTo_IdAndStatus(Long employeeId, Project.ProjectStatus status);
}