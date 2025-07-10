package api.example.hrm_system.Project;

import api.example.hrm_system.employee.Employee;
import api.example.hrm_system.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public String assignProjectToEmployee(AssignmentDTO assignmentDTO) {
        Project project = projectRepository.findById(Math.toIntExact(assignmentDTO.getProjectId()))
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + assignmentDTO.getProjectId()));

        Employee employee = employeeRepository.findById(assignmentDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + assignmentDTO.getEmployeeId()));

        // Check if the employee is already assigned to this project
        if (project.getAssignedTo() != null && project.getAssignedTo().getId().equals(employee.getId())) {
            throw new RuntimeException("Employee is already assigned to this project");
        }

        project.setAssignedTo(employee);
        projectRepository.save(project);

        return "Project '" + project.getName() + "' successfully assigned to employee: " + employee.getFullName();
    }
}