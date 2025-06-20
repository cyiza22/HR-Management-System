package api.example.hrm_system.employee;

import api.example.hrm_system.employee.EmployeeDashboardDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeDashboardDto> getAllForDashboard() {
        return employeeRepository.findAll().stream().map(emp ->
                new EmployeeDashboardDto(
                        emp.getEmployeeId(),
                        emp.getFirstName() + " " + emp.getLastName(),
                        emp.getDepartment().getName(),
                        emp.getDesignation(),
                        emp.getEmployeeType(),
                        emp.getStatus()
                )
        ).collect(Collectors.toList());
    }

}

