package api.example.hrm_system.department;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id)
                .map(dto -> ResponseEntity.ok(dto))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-name")
    public ResponseEntity<DepartmentDTO> getDepartmentByName(@RequestParam String departmentName) {
        return departmentService.getDepartmentByName(departmentName)
                .map(dto -> ResponseEntity.ok(dto))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-work-type")
    public ResponseEntity<List<DepartmentDTO>> getDepartmentsByWorkType(@RequestParam Department.WorkType workType) {
        return ResponseEntity.ok(departmentService.getDepartmentsByWorkType(workType));
    }

    @GetMapping("/by-employment-status")
    public ResponseEntity<List<DepartmentDTO>> getDepartmentsByEmploymentStatus(@RequestParam Department.EmploymentStatus employmentStatus) {
        return ResponseEntity.ok(departmentService.getDepartmentsByEmploymentStatus(employmentStatus));
    }

    @GetMapping("/by-location")
    public ResponseEntity<List<DepartmentDTO>> getDepartmentsByLocation(@RequestParam String location) {
        return ResponseEntity.ok(departmentService.getDepartmentsByLocation(location));
    }

    @GetMapping("/by-manager/{managerId}")
    public ResponseEntity<List<DepartmentDTO>> getDepartmentsByManagerId(@PathVariable Long managerId) {
        return ResponseEntity.ok(departmentService.getDepartmentsByManagerId(managerId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DepartmentDTO>> searchDepartmentsByName(@RequestParam String name) {
        return ResponseEntity.ok(departmentService.searchDepartmentsByName(name));
    }

    @PostMapping("/create")
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody Department department) {
        DepartmentDTO createdDepartment = departmentService.createDepartment(department);
        return new ResponseEntity<>(createdDepartment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(
            @PathVariable Long id,
            @RequestBody Department departmentDetails) {
        return departmentService.updateDepartment(id, departmentDetails)
                .map(dto -> ResponseEntity.ok(dto))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        if (departmentService.deleteDepartment(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}