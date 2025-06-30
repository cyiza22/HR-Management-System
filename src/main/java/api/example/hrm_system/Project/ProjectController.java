package api.example.hrm_system.Project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/new")
    public ResponseEntity<Project> createProject(@RequestBody ProjectDTO dto) {
        return ResponseEntity.ok(projectService.createProject(dto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Integer id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @GetMapping("/employee")
    public ResponseEntity<List<Project>> getProjectsByEmployee(@RequestParam Long employeeId) {
        return ResponseEntity.ok(projectService.getProjectsByEmployeeId(employeeId));
    }

    @PutMapping("/update")
    public ResponseEntity<Project> updateProject(@RequestBody Project project, @RequestParam Integer id) {
        return ResponseEntity.ok(projectService.updateProject(project, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
