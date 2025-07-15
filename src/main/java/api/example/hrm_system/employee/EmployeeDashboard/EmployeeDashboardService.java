package api.example.hrm_system.employee.EmployeeDashboard;

import api.example.hrm_system.employee.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeDashboardService {

    private final EmployeeDashboardRepository employeeDashboardRepository;


    public EmployeeDashboardService(EmployeeDashboardRepository employeeDashboardRepository) {
        this.employeeDashboardRepository = employeeDashboardRepository;
    }

    public List<EmployeeDashboardDTO> getAllEmployees() {
        return employeeDashboardRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<EmployeeDashboardDTO> getAllEmployeesPaginated(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return employeeDashboardRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    public EmployeeDashboardDTO getEmployeeById(Long id) {
        return employeeDashboardRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    public EmployeeDashboardDTO getEmployeeByName(String firstName, String lastName) {
        return employeeDashboardRepository.findByFirstNameAndLastName(firstName, lastName)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Employee not found with name: " + firstName + " " + lastName));
    }

    public EmployeeDashboardDTO getEmployeeByEmployeeId(String employeeId) {
        return employeeDashboardRepository.findByEmployeeId(employeeId)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Employee not found with employee ID: " + employeeId));
    }

    public List<EmployeeDashboardDTO> searchEmployees(String searchTerm) {
        return employeeDashboardRepository.searchEmployees(searchTerm).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDashboardDTO> searchByFirstName(String firstName) {
        return employeeDashboardRepository.findByFirstNameContainingIgnoreCase(firstName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDashboardDTO> searchByLastName(String lastName) {
        return employeeDashboardRepository.findByLastNameContainingIgnoreCase(lastName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDashboardDTO> searchByFullName(String fullName) {
        return employeeDashboardRepository.findByFullNameContainingIgnoreCase(fullName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDashboardDTO> getEmployeesByDepartment(String departmentName) {
        return employeeDashboardRepository.findByDepartmentName(departmentName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDashboardDTO> getEmployeesByDesignation(String designation) {
        return employeeDashboardRepository.findByDesignation(designation).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDashboardDTO> getEmployeesByType(String employeeType) {
        return employeeDashboardRepository.findByEmployeeType(employeeType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDashboardDTO> getEmployeesByStatus(String status) {
        return employeeDashboardRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDashboardDTO> getEmployeesByFilters(String department, String designation,
                                                            String employeeType, String status) {
        return employeeDashboardRepository.findByMultipleFilters(department, designation, employeeType, status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public long getEmployeeCountByDepartment(String departmentName) {
        return employeeDashboardRepository.countByDepartmentName(departmentName);
    }

    public long getEmployeeCountByStatus(String status) {
        return employeeDashboardRepository.countByStatus(status);
    }

    public long getEmployeeCountByType(String employeeType) {
        return employeeDashboardRepository.countByEmployeeType(employeeType);
    }

    public EmployeeDashboardDTO getEmployeeByEmail(String email) {
        return employeeDashboardRepository.findByEmail(email)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));
    }

    public long getTotalEmployeeCount() {
        return employeeDashboardRepository.count();
    }

    // Update convertToDto method with null checks
    private EmployeeDashboardDTO convertToDto(Employee employee) {
        if (employee == null) return null;

        EmployeeDashboardDTO dto = new EmployeeDashboardDTO();
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setFullName(employee.getFirstName() + " " + employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setMobileNumber(employee.getMobileNumber());

        if (employee.getDepartment() != null) {
            dto.setDepartment(employee.getDepartment().getDepartmentName());
        }

        dto.setDesignation(employee.getDesignation());
        dto.setEmployeeType(employee.getEmployeeType());
        dto.setStatus(employee.getStatus());

        return dto;
    }
}
