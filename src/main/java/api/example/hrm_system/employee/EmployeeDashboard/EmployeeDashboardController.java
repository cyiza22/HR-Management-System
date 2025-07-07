package api.example.hrm_system.employee.EmployeeDashboard;


import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeDashboardController {


    private EmployeeDashboardService employeeDashboardService;

    // Get all employees (non-paginated)
    @GetMapping
    public ResponseEntity<List<EmployeeDashboardDTO>> getAllEmployees() {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.getAllEmployees();
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get all employees with pagination and sorting
    @GetMapping("/paginated")
    public ResponseEntity<Page<EmployeeDashboardDTO>> getAllEmployeesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<EmployeeDashboardDTO> employees = employeeDashboardService
                    .getAllEmployeesPaginated(page, size, sortBy, sortDir);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDashboardDTO> getEmployeeById(@PathVariable Long id) {
        try {
            EmployeeDashboardDTO employee = employeeDashboardService.getEmployeeById(id);
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get employee by employee ID
    @GetMapping("/employee-id/{employeeId}")
    public ResponseEntity<EmployeeDashboardDTO> getEmployeeByEmployeeId(@PathVariable String employeeId) {
        try {
            EmployeeDashboardDTO employee = employeeDashboardService.getEmployeeByEmployeeId(employeeId);
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get employee by name
    @GetMapping("/name")
    public ResponseEntity<EmployeeDashboardDTO> getEmployeeByName(
            @RequestParam String firstName,
            @RequestParam String lastName) {
        try {
            EmployeeDashboardDTO employee = employeeDashboardService.getEmployeeByName(firstName, lastName);
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Search employees by general search term
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeDashboardDTO>> searchEmployees(
            @RequestParam String searchTerm) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.searchEmployees(searchTerm);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Search by first name
    @GetMapping("/search/first-name")
    public ResponseEntity<List<EmployeeDashboardDTO>> searchByFirstName(
            @RequestParam String firstName) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.searchByFirstName(firstName);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Search by last name
    @GetMapping("/search/last-name")
    public ResponseEntity<List<EmployeeDashboardDTO>> searchByLastName(
            @RequestParam String lastName) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.searchByLastName(lastName);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Search by full name
    @GetMapping("/search/full-name")
    public ResponseEntity<List<EmployeeDashboardDTO>> searchByFullName(
            @RequestParam String fullName) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.searchByFullName(fullName);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get employees by department
    @GetMapping("/department/{departmentName}")
    public ResponseEntity<List<EmployeeDashboardDTO>> getEmployeesByDepartment(
            @PathVariable String departmentName) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.getEmployeesByDepartment(departmentName);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get employees by designation
    @GetMapping("/designation/{designation}")
    public ResponseEntity<List<EmployeeDashboardDTO>> getEmployeesByDesignation(
            @PathVariable String designation) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.getEmployeesByDesignation(designation);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get employees by type
    @GetMapping("/type/{employeeType}")
    public ResponseEntity<List<EmployeeDashboardDTO>> getEmployeesByType(
            @PathVariable String employeeType) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.getEmployeesByType(employeeType);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get employees by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmployeeDashboardDTO>> getEmployeesByStatus(
            @PathVariable String status) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.getEmployeesByStatus(status);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get employees by multiple filters
    @GetMapping("/filter")
    public ResponseEntity<List<EmployeeDashboardDTO>> getEmployeesByFilters(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String designation,
            @RequestParam(required = false) String employeeType,
            @RequestParam(required = false) String status) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService
                    .getEmployeesByFilters(department, designation, employeeType, status);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get employee count by department
    @GetMapping("/count/department/{departmentName}")
    public ResponseEntity<Long> getEmployeeCountByDepartment(@PathVariable String departmentName) {
        try {
            long count = employeeDashboardService.getEmployeeCountByDepartment(departmentName);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get employee count by status
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> getEmployeeCountByStatus(@PathVariable String status) {
        try {
            long count = employeeDashboardService.getEmployeeCountByStatus(status);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get employee count by type
    @GetMapping("/count/type/{employeeType}")
    public ResponseEntity<Long> getEmployeeCountByType(@PathVariable String employeeType) {
        try {
            long count = employeeDashboardService.getEmployeeCountByType(employeeType);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get total employee count
    @GetMapping("/count/total")
    public ResponseEntity<Long> getTotalEmployeeCount() {
        try {
            long count = employeeDashboardService.getTotalEmployeeCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}