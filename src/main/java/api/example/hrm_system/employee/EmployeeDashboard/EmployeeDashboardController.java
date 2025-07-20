package api.example.hrm_system.employee.EmployeeDashboard;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeDashboardController {

    private final EmployeeDashboardService employeeDashboardService;

    public EmployeeDashboardController(EmployeeDashboardService employeeDashboardService) {
        this.employeeDashboardService = employeeDashboardService;
    }

    // EMPLOYEE ENDPOINTS - No @PreAuthorize to avoid conflicts with SecurityConfig
    @GetMapping("/my-profile")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Debug authentication
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("=== MY-PROFILE DEBUG ===");
            System.out.println("UserDetails: " + userDetails);
            System.out.println("Authentication: " + auth);
            System.out.println("Authorities: " + (auth != null ? auth.getAuthorities() : "null"));
            System.out.println("========================");

            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not authenticated"));
            }

            EmployeeDashboardDTO employee = employeeDashboardService.getEmployeeByEmail(userDetails.getUsername());
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Employee profile not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/my-dashboard")
    public ResponseEntity<?> getMyDashboard(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Debug authentication
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("=== MY-DASHBOARD DEBUG ===");
            System.out.println("UserDetails: " + userDetails);
            System.out.println("Authentication: " + auth);
            System.out.println("Authorities: " + (auth != null ? auth.getAuthorities() : "null"));
            System.out.println("===========================");

            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not authenticated"));
            }

            EmployeeDashboardDTO employee = employeeDashboardService.getEmployeeByEmail(userDetails.getUsername());
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Employee dashboard not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }

    // Test endpoint without @PreAuthorize to see if it's an authorization issue
    @GetMapping("/test-no-auth")
    public ResponseEntity<?> testNoAuth(@AuthenticationPrincipal UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(Map.of(
                "message", "Test endpoint reached!",
                "userDetails", userDetails != null ? userDetails.getUsername() : "null",
                "authentication", auth != null ? auth.isAuthenticated() : false,
                "authorities", auth != null ? auth.getAuthorities().toString() : "null"
        ));
    }

    // MANAGER AND HR ENDPOINTS
    @GetMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<?> getAllEmployees() {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.getAllEmployees();
            return ResponseEntity.ok(Map.of(
                    "employees", employees,
                    "total", employees.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve employees: " + e.getMessage()));
        }
    }

    @GetMapping("/paginated")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<?> getAllEmployeesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<EmployeeDashboardDTO> employees = employeeDashboardService
                    .getAllEmployeesPaginated(page, size, sortBy, sortDir);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve paginated employees: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        try {
            EmployeeDashboardDTO employee = employeeDashboardService.getEmployeeById(id);
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Employee not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/employee-id/{employeeId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<?> getEmployeeByEmployeeId(@PathVariable String employeeId) {
        try {
            EmployeeDashboardDTO employee = employeeDashboardService.getEmployeeByEmployeeId(employeeId);
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Employee not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/name")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<?> getEmployeeByName(
            @RequestParam String firstName,
            @RequestParam String lastName) {
        try {
            EmployeeDashboardDTO employee = employeeDashboardService.getEmployeeByName(firstName, lastName);
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Employee not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<?> searchEmployees(@RequestParam String searchTerm) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.searchEmployees(searchTerm);
            return ResponseEntity.ok(Map.of(
                    "employees", employees,
                    "total", employees.size(),
                    "searchTerm", searchTerm
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Search failed: " + e.getMessage()));
        }
    }

    @GetMapping("/search/first-name")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<?> searchByFirstName(@RequestParam String firstName) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.searchByFirstName(firstName);
            return ResponseEntity.ok(Map.of(
                    "employees", employees,
                    "total", employees.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Search by first name failed: " + e.getMessage()));
        }
    }

    @GetMapping("/search/last-name")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<?> searchByLastName(@RequestParam String lastName) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.searchByLastName(lastName);
            return ResponseEntity.ok(Map.of(
                    "employees", employees,
                    "total", employees.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Search by last name failed: " + e.getMessage()));
        }
    }

    @GetMapping("/search/full-name")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<?> searchByFullName(@RequestParam String fullName) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.searchByFullName(fullName);
            return ResponseEntity.ok(Map.of(
                    "employees", employees,
                    "total", employees.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Search by full name failed: " + e.getMessage()));
        }
    }

    @GetMapping("/department/{departmentName}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<?> getEmployeesByDepartment(@PathVariable String departmentName) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.getEmployeesByDepartment(departmentName);
            return ResponseEntity.ok(Map.of(
                    "employees", employees,
                    "total", employees.size(),
                    "department", departmentName
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get employees by department: " + e.getMessage()));
        }
    }

    @GetMapping("/designation/{designation}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<?> getEmployeesByDesignation(@PathVariable String designation) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.getEmployeesByDesignation(designation);
            return ResponseEntity.ok(Map.of(
                    "employees", employees,
                    "total", employees.size(),
                    "designation", designation
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get employees by designation: " + e.getMessage()));
        }
    }

    @GetMapping("/type/{employeeType}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<?> getEmployeesByType(@PathVariable String employeeType) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.getEmployeesByType(employeeType);
            return ResponseEntity.ok(Map.of(
                    "employees", employees,
                    "total", employees.size(),
                    "employeeType", employeeType
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get employees by type: " + e.getMessage()));
        }
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<?> getEmployeesByStatus(@PathVariable String status) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService.getEmployeesByStatus(status);
            return ResponseEntity.ok(Map.of(
                    "employees", employees,
                    "total", employees.size(),
                    "status", status
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get employees by status: " + e.getMessage()));
        }
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<?> getEmployeesByFilters(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String designation,
            @RequestParam(required = false) String employeeType,
            @RequestParam(required = false) String status) {
        try {
            List<EmployeeDashboardDTO> employees = employeeDashboardService
                    .getEmployeesByFilters(department, designation, employeeType, status);
            return ResponseEntity.ok(Map.of(
                    "employees", employees,
                    "total", employees.size(),
                    "filters", Map.of(
                            "department", department != null ? department : "all",
                            "designation", designation != null ? designation : "all",
                            "employeeType", employeeType != null ? employeeType : "all",
                            "status", status != null ? status : "all"
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to filter employees: " + e.getMessage()));
        }
    }

    @GetMapping("/count/department/{departmentName}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<?> getEmployeeCountByDepartment(@PathVariable String departmentName) {
        try {
            long count = employeeDashboardService.getEmployeeCountByDepartment(departmentName);
            return ResponseEntity.ok(Map.of(
                    "count", count,
                    "department", departmentName
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get employee count: " + e.getMessage()));
        }
    }

    @GetMapping("/count/status/{status}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<?> getEmployeeCountByStatus(@PathVariable String status) {
        try {
            long count = employeeDashboardService.getEmployeeCountByStatus(status);
            return ResponseEntity.ok(Map.of(
                    "count", count,
                    "status", status
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get employee count by status: " + e.getMessage()));
        }
    }

    @GetMapping("/count/type/{employeeType}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<?> getEmployeeCountByType(@PathVariable String employeeType) {
        try {
            long count = employeeDashboardService.getEmployeeCountByType(employeeType);
            return ResponseEntity.ok(Map.of(
                    "count", count,
                    "employeeType", employeeType
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get employee count by type: " + e.getMessage()));
        }
    }

    @GetMapping("/count/total")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<?> getTotalEmployeeCount() {
        try {
            long count = employeeDashboardService.getTotalEmployeeCount();
            return ResponseEntity.ok(Map.of("totalEmployees", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get total employee count: " + e.getMessage()));
        }
    }
}