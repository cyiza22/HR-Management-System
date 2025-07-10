package api.example.hrm_system.department;

import api.example.hrm_system.employee.Employee;
import api.example.hrm_system.employee.EmployeeDashboard.EmployeeDashboardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeDashboardRepository employeeDashboardRepository;

    public DepartmentService(DepartmentRepository departmentRepository, EmployeeDashboardRepository employeeDashboardRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeDashboardRepository = employeeDashboardRepository;
    }

    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<DepartmentDTO> getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(this::mapToDTO);
    }

    public Optional<DepartmentDTO> getDepartmentByName(String departmentName) {
        return departmentRepository.findByDepartmentName(departmentName)
                .map(this::mapToDTO);
    }

    public List<DepartmentDTO> getDepartmentsByWorkType(Department.WorkType workType) {
        return departmentRepository.findByWorkType(workType).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<DepartmentDTO> getDepartmentsByEmploymentStatus(Department.EmploymentStatus employmentStatus) {
        return departmentRepository.findByEmploymentStatus(employmentStatus).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<DepartmentDTO> getDepartmentsByLocation(String location) {
        return departmentRepository.findByLocation(location).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<DepartmentDTO> getDepartmentsByManagerId(Long managerId) {
        return departmentRepository.findByManagerId(managerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<DepartmentDTO> searchDepartmentsByName(String name) {
        return departmentRepository.findByDepartmentNameContaining(name).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public DepartmentDTO createDepartment(Department department) {
        Department savedDepartment = departmentRepository.save(department);
        return mapToDTO(savedDepartment);
    }
    public DepartmentDTO getDepartmentByManagerEmail(String username) {
        Employee manager = employeeDashboardRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Manager not found with email: " + username));
        Department department = departmentRepository.findByManagerId(manager.getId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Department not found for manager with email: " + username));

        return mapToDTO(department);
    }

    public Optional<DepartmentDTO> updateDepartment(Long id, Department departmentDetails) {
        return departmentRepository.findById(id)
                .map(department -> {
                    department.setDepartmentName(departmentDetails.getDepartmentName());
                    department.setLocation(departmentDetails.getLocation());
                    department.setManager(departmentDetails.getManager());
                    department.setWorkType(departmentDetails.getWorkType());
                    department.setEmploymentStatus(departmentDetails.getEmploymentStatus());
                    Department updatedDepartment = departmentRepository.save(department);
                    return mapToDTO(updatedDepartment);
                });
    }

    public boolean deleteDepartment(Long id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private DepartmentDTO mapToDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setDepartmentName(department.getDepartmentName());
        dto.setLocation(department.getLocation());
        dto.setWorkType(department.getWorkType());
        dto.setEmploymentStatus(department.getEmploymentStatus());
        dto.setCreatedAt(department.getCreatedAt());
        dto.setUpdatedAt(department.getUpdatedAt());

        // Set manager name if manager exists
        if (department.getManager() != null) {
            Employee manager = department.getManager();
            dto.setManagerName(manager.getFirstName() + " " + manager.getLastName());
        }

        // Set total employees count
        if (department.getEmployees() != null) {
            dto.setTotalEmployees(department.getEmployees().size());
        } else {
            dto.setTotalEmployees(0);
        }

        return dto;
    }


}