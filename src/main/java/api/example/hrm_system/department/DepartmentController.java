package api.example.hrm_system.department;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    // HR-only endpoints
    @PostMapping
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody Department department) {
        return ResponseEntity.ok(departmentService.createDepartment(department));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<Optional<DepartmentDTO>> updateDepartment(
            @PathVariable Long id,
            @RequestBody Department department) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, department));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    // Manager and HR endpoints
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<Optional<DepartmentDTO>> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @GetMapping("/my-department")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<DepartmentDTO> getMyDepartment(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(departmentService.getDepartmentByManagerEmail(userDetails.getUsername()));
    }

    // Public endpoints
    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }
}