package api.example.hrm_system.Leave;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave")
public class LeaveController {

    private final LeaveService leaveService;

    @Autowired
    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/apply")
    public ResponseEntity<Leave> applyLeave(@RequestBody LeaveDTO dto) {
        return ResponseEntity.ok(leaveService.applyLeave(dto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Leave>> getAllLeaves() {
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Leave> getLeaveById(@PathVariable Integer id) {
        return ResponseEntity.ok(leaveService.getLeaveById(id));
    }

    @GetMapping("/employee")
    public ResponseEntity<List<Leave>> getLeavesByEmployee(@RequestParam Long employeeId) {
        return ResponseEntity.ok(leaveService.getLeavesByEmployee(employeeId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Leave> updateLeave(@PathVariable Integer id, @RequestBody LeaveDTO dto) {
        return ResponseEntity.ok(leaveService.updateLeave(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLeave(@PathVariable Integer id) {
        leaveService.deleteLeave(id);
        return ResponseEntity.noContent().build();
    }
}
