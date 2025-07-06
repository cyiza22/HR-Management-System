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


    private EmployeeDashboardRepository employeeDashboardRepository;

    public List<EmployeeDashboardDTO> getAllEmployees() {
        List<Employee> employees = employeeDashboardRepository.findAll();
        return employees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<EmployeeDashboardDTO> getAllEmployeesPaginated(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Employee> employeePage = employeeDashboardRepository.findAll(pageable);

        return employeePage.map(this::convertToDto);
    }

    public EmployeeDashboardDTO getEmployeeById(Long id) {
        Optional<Employee> employee = employeeDashboardRepository.findById(id);
        if (employee.isPresent()) {
            return convertToDto(employee.get());
        }
        throw new RuntimeException("Employee not found with id: " + id);
    }

    public EmployeeDashboardDTO getEmployeeByName(String firstName, String lastName) {
        Optional<Employee> employee = employeeDashboardRepository.findByFirstNameAndLastName(firstName, lastName);
        if (employee.isPresent()) {
            return convertToDto(employee.get());
        }
        throw new RuntimeException("Employee not found with name: " + firstName + " " + lastName);
    }

    public EmployeeDashboardDTO getEmployeeByEmployeeId(String employeeId) {
        Optional<Employee> employee = employeeDashboardRepository.findByEmployeeId(employeeId);
        if (employee.isPresent()) {
            return convertToDto(employee.get());
        }
        throw new RuntimeException("Employee not found with employee ID: " + employeeId);
    }

    public List<EmployeeDashboardDTO> searchEmployees(String searchTerm) {
        List<Employee> employees = employeeDashboardRepository.searchEmployees(searchTerm);
        return employees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDashboardDTO> searchByFirstName(String firstName) {
        List<Employee> employees = employeeDashboardRepository.findByFirstNameContainingIgnoreCase(firstName);
        return employees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDashboardDTO> searchByLastName(String lastName) {
        List<Employee> employees = employeeDashboardRepository.findByLastNameContainingIgnoreCase(lastName);
        return employees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDashboardDTO> searchByFullName(String fullName) {
        List<Employee> employees = employeeDashboardRepository.findByFullNameContainingIgnoreCase(fullName);
        return employees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDashboardDTO> getEmployeesByDepartment(String departmentName) {
        List<Employee> employees = employeeDashboardRepository.findByDepartmentName(departmentName);
        return employees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDashboardDTO> getEmployeesByDesignation(String designation) {
        List<Employee> employees = employeeDashboardRepository.findByDesignation(designation);
        return employees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDashboardDTO> getEmployeesByType(String employeeType) {
        List<Employee> employees = employeeDashboardRepository.findByEmployeeType(employeeType);
        return employees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDashboardDTO> getEmployeesByStatus(String status) {
        List<Employee> employees = employeeDashboardRepository.findByStatus(status);
        return employees.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDashboardDTO> getEmployeesByFilters(String department, String designation,
                                                            String employeeType, String status) {
        List<Employee> employees = employeeDashboardRepository.findByMultipleFilters(
                department, designation, employeeType, status);
        return employees.stream()
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

    public long getTotalEmployeeCount() {
        return employeeDashboardRepository.count();
    }



    private EmployeeDashboardDTO convertToDto(Employee employee) {
        EmployeeDashboardDTO dto = new EmployeeDashboardDTO();
        dto.setId(employee.getId());
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setFullName(employee.getFirstName() + " " + employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setMobileNumber(employee.getMobileNumber());
        dto.setDepartment(employee.getDepartment() != null ? employee.getDepartment().getDepartmentName() : null);
        dto.setDesignation(employee.getDesignation());
        dto.setEmployeeType(employee.getEmployeeType());
        dto.setStatus(employee.getStatus());
        return dto;
    }
}


