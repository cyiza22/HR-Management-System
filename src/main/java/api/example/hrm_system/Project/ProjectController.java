package api.example.hrm_system.Project;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // Manager and HR endpoints
    @PostMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectDTO dto) {
        return ResponseEntity.ok(projectService.createProject(dto));
    }

    @PostMapping("/assign")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<String> assignProject(@Valid @RequestBody AssignmentDTO dto) {
        return ResponseEntity.ok(projectService.assignProject(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Integer id, @Valid @RequestBody ProjectDTO dto) {
        return ResponseEntity.ok(projectService.updateProject(dto, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    // Employee endpoints
    @GetMapping("/my-projects")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'MANAGER', 'HR')")
    public ResponseEntity<List<ProjectDTO>> getMyProjects(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(projectService.getProjectsByEmployeeEmail(userDetails.getUsername()));
    }

    // Manager endpoints
    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<List<ProjectDTO>> getDepartmentProjects(@PathVariable Long departmentId) {
        return ResponseEntity.ok(projectService.getProjectsByDepartment(departmentId));
    }

    // Public endpoints (read-only) - accessible by all authenticated users
    @GetMapping
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'MANAGER', 'HR')")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'MANAGER', 'HR')")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Integer id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }
}