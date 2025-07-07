package api.example.hrm_system.Project;

import api.example.hrm_system.employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, EmployeeRepository employeeRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }

    public Project createProject(ProjectDTO dto) {
        Employee employee = employeeRepository.findById(Long.valueOf(dto.getEmployeeId()))
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Project project = new Project();
        project.setName(dto.getName());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setStatus(Project.ProjectStatus.valueOf(dto.getStatus()));
        project.setAssignedTo(employee);

        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getProjectsByEmployeeId(long employeeId) {
        return projectRepository.findByAssignedToId(employeeId);
    }

    public Project getProjectById(Integer id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public Project updateProject(Project updated, Integer id) {
        Project project = getProjectById(id);
        project.setName(updated.getName());
        project.setStartDate(updated.getStartDate());
        project.setEndDate(updated.getEndDate());
        project.setStatus(updated.getStatus());
        project.setAssignedTo(updated.getAssignedTo());
        return projectRepository.save(project);
    }

    public void deleteProject(Integer id) {
        projectRepository.deleteById(id);
    }
}
