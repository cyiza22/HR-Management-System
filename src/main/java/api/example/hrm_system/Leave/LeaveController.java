package api.example.hrm_system.Leave;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<LeaveDTO> applyLeave(
            @Valid @RequestBody LeaveDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(leaveService.applyLeave(dto, userDetails.getUsername()));
    }

    @GetMapping("/my-leaves")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<LeaveDTO>> getMyLeaves(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(leaveService.getLeavesByEmployeeEmail(userDetails.getUsername()));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<LeaveDTO>> getPendingLeaves(@RequestParam Long departmentId) {
        return ResponseEntity.ok(leaveService.getPendingLeavesByDepartment(departmentId));
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<LeaveDTO> approveLeave(@PathVariable Integer id) {
        return ResponseEntity.ok(leaveService.approveLeave(id));
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<LeaveDTO> rejectLeave(@PathVariable Integer id, @RequestParam String reason) {
        return ResponseEntity.ok(leaveService.rejectLeave(id, reason));
    }

    @GetMapping
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<List<LeaveDTO>> getAllLeaves() {
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }
}