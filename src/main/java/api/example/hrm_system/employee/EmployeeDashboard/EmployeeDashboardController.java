package api.example.hrm_system.employee.EmployeeDashboard;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeDashboardController {

    private final EmployeeDashboardService employeeDashboardService;

    // Constructor injection
    public EmployeeDashboardController(EmployeeDashboardService employeeDashboardService) {
        this.employeeDashboardService = employeeDashboardService;
    }

    // Employee endpoints - can access their own data
    @GetMapping("/my-profile")
    @PreAuthorize("hasAnyRole('HR', 'EMPLOYEE')")
    public ResponseEntity<EmployeeDashboardDTO> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            EmployeeDashboardDTO employee = employeeDashboardService.getEmployeeByEmail(userDetails.getUsername());
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/my-dashboard")
    @PreAuthorize("hasAnyRole('HR', 'EMPLOYEE')")
    public ResponseEntity<EmployeeDashboardDTO> getMyDashboard(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            EmployeeDashboardDTO employee = employeeDashboardService.getEmployeeByEmail(userDetails.getUsername());
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Manager and HR endpoints - can access all employee data
    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<List<EmployeeDashboardDTO>> getAllEmployees() {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.getAllEmployees();
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/paginated")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
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

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
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

    @GetMapping("/employee-id/{employeeId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
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

    @GetMapping("/name")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
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

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<List<EmployeeDashboardDTO>> searchEmployees(@RequestParam String searchTerm) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.searchEmployees(searchTerm);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search/first-name")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<List<EmployeeDashboardDTO>> searchByFirstName(@RequestParam String firstName) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.searchByFirstName(firstName);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search/last-name")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<List<EmployeeDashboardDTO>> searchByLastName(@RequestParam String lastName) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.searchByLastName(lastName);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search/full-name")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<List<EmployeeDashboardDTO>> searchByFullName(@RequestParam String fullName) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.searchByFullName(fullName);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/department/{departmentName}")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<List<EmployeeDashboardDTO>> getEmployeesByDepartment(@PathVariable String departmentName) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.getEmployeesByDepartment(departmentName);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/designation/{designation}")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<List<EmployeeDashboardDTO>> getEmployeesByDesignation(@PathVariable String designation) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.getEmployeesByDesignation(designation);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/type/{employeeType}")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<List<EmployeeDashboardDTO>> getEmployeesByType(@PathVariable String employeeType) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.getEmployeesByType(employeeType);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<List<EmployeeDashboardDTO>> getEmployeesByStatus(@PathVariable String status) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.getEmployeesByStatus(status);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
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

    @GetMapping("/count/department/{departmentName}")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<Long> getEmployeeCountByDepartment(@PathVariable String departmentName) {
        try {
            long count = employeeDashboardService.getEmployeeCountByDepartment(departmentName);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/count/status/{status}")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<Long> getEmployeeCountByStatus(@PathVariable String status) {
        try {
            long count = employeeDashboardService.getEmployeeCountByStatus(status);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/count/type/{employeeType}")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<Long> getEmployeeCountByType(@PathVariable String employeeType) {
        try {
            long count = employeeDashboardService.getEmployeeCountByType(employeeType);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/count/total")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<Long> getTotalEmployeeCount() {
        try {
            long count = employeeDashboardService.getTotalEmployeeCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}