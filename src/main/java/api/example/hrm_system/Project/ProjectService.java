package api.example.hrm_system.Project;

import api.example.hrm_system.employee.Employee;
import api.example.hrm_system.employee.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        try {
            Employee employee = employeeRepository.findByEmail(email)
                    .orElse(null);

            if (employee == null) {
                log.warn("Employee not found with email: {}", email);
                return Collections.emptyList();
            }

            return projectRepository.findByAssignedTo_Id(employee.getId()).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting projects for employee email {}: {}", email, e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<ProjectDTO> getProjectsByDepartment(Long departmentId) {
        try {
            return projectRepository.findAll().stream()
                    .filter(project -> project.getAssignedTo() != null &&
                            project.getAssignedTo().getDepartment() != null &&
                            project.getAssignedTo().getDepartment().getId().equals(departmentId))
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting projects for department {}: {}", departmentId, e.getMessage());
            return Collections.emptyList();
        }
    }

    @Transactional
    public ProjectDTO createProject(ProjectDTO dto) {
        try {
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
            log.info("Project created successfully: {}", savedProject.getName());
            return convertToDTO(savedProject);
        } catch (Exception e) {
            log.error("Error creating project: {}", e.getMessage());
            throw new RuntimeException("Failed to create project: " + e.getMessage());
        }
    }

    private void validateProjectDates(LocalDate startDate, LocalDate endDate) {
        if (endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }

    public List<ProjectDTO> getAllProjects() {
        try {
            return projectRepository.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting all projects: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public ProjectDTO getProjectById(Integer id) {
        try {
            Project project = projectRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));
            return convertToDTO(project);
        } catch (Exception e) {
            log.error("Error getting project by ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Project not found with ID: " + id);
        }
    }

    @Transactional
    public ProjectDTO updateProject(ProjectDTO updated, Integer id) {
        try {
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
            log.info("Project updated successfully: {}", savedProject.getName());
            return convertToDTO(savedProject);
        } catch (Exception e) {
            log.error("Error updating project {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to update project: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteProject(Integer id) {
        try {
            if (!projectRepository.existsById(id)) {
                throw new RuntimeException("Project not found with ID: " + id);
            }
            projectRepository.deleteById(id);
            log.info("Project deleted successfully: {}", id);
        } catch (Exception e) {
            log.error("Error deleting project {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to delete project: " + e.getMessage());
        }
    }

    @Transactional
    public String assignProject(AssignmentDTO assignmentDTO) {
        try {
            return assignmentService.assignProjectToEmployee(assignmentDTO);
        } catch (Exception e) {
            log.error("Error assigning project: {}", e.getMessage());
            throw new RuntimeException("Failed to assign project: " + e.getMessage());
        }
    }

    private ProjectDTO convertToDTO(Project project) {
        try {
            ProjectDTO dto = new ProjectDTO();
            dto.setId(project.getId());
            dto.setName(project.getName());
            dto.setStartDate(project.getStartDate());
            dto.setEndDate(project.getEndDate());
            dto.setStatus(project.getStatus());

            if (project.getAssignedTo() != null) {
                dto.setEmployeeId(project.getAssignedTo().getId());

                // Add department ID if available
                if (project.getAssignedTo().getDepartment() != null) {
                    dto.setDepartmentId(project.getAssignedTo().getDepartment().getId());
                }
            }

            return dto;
        } catch (Exception e) {
            log.error("Error converting project to DTO: {}", e.getMessage());
            throw new RuntimeException("Failed to convert project data");
        }
    }
}