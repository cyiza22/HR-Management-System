package api.example.hrm_system.Project;

import api.example.hrm_system.employee.Employee;
import api.example.hrm_system.employee.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final AssignmentService assignmentService;

    public ProjectService(ProjectRepository projectRepository,
                          EmployeeRepository employeeRepository,
                          AssignmentService assignmentService) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.assignmentService = assignmentService;
    }

    public List<ProjectDTO> getProjectsByEmployeeEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));

        return projectRepository.findByAssignedTo_Id(employee.getId()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> getProjectsByDepartment(Long departmentId) {
        return projectRepository.findAll().stream()
                .filter(project -> project.getAssignedTo() != null &&
                        project.getAssignedTo().getDepartment() != null &&
                        project.getAssignedTo().getDepartment().getId().equals(departmentId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Rest of the existing methods remain the same
    @Transactional
    public ProjectDTO createProject(ProjectDTO dto) {
        validateProjectDates(dto.getStartDate(), dto.getEndDate());

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + dto.getEmployeeId()));

        Project project = new Project();
        project.setName(dto.getName());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setStatus(dto.getStatus());
        project.setAssignedTo(employee);

        Project savedProject = projectRepository.save(project);
        return convertToDTO(savedProject);
    }

    private void validateProjectDates(LocalDate startDate, LocalDate endDate) {
        if (endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }

    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProjectDTO getProjectById(Integer id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));
        return convertToDTO(project);
    }

    @Transactional
    public ProjectDTO updateProject(ProjectDTO updated, Integer id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));

        validateProjectDates(updated.getStartDate(), updated.getEndDate());

        Employee employee = employeeRepository.findById(updated.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + updated.getEmployeeId()));

        project.setName(updated.getName());
        project.setStartDate(updated.getStartDate());
        project.setEndDate(updated.getEndDate());
        project.setStatus(updated.getStatus());
        project.setAssignedTo(employee);

        Project savedProject = projectRepository.save(project);
        return convertToDTO(savedProject);
    }

    @Transactional
    public void deleteProject(Integer id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found with ID: " + id);
        }
        projectRepository.deleteById(id);
    }

    @Transactional
    public String assignProject(AssignmentDTO assignmentDTO) {
        return assignmentService.assignProjectToEmployee(assignmentDTO);
    }

    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setStatus(project.getStatus());
        dto.setEmployeeId(project.getAssignedTo().getId());

        // Add department ID if available
        if (project.getAssignedTo().getDepartment() != null) {
            dto.setDepartmentId(project.getAssignedTo().getDepartment().getId());
        }

        return dto;
    }
}