package api.example.hrm_system.employee;

import api.example.hrm_system.employee.EmployeeDashboardDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/dashboard")
    public List<EmployeeDashboardDto> getDashboardData() {
        return employeeService.getAllForDashboard();
    }
}
