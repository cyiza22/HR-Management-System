package api.example.hrm_system.Project;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByAssignedTo_Id(Long employeeId);
    List<Project> findByStatus(Project.ProjectStatus status);
    List<Project> findByAssignedTo_IdAndStatus(Long employeeId, Project.ProjectStatus status);
}