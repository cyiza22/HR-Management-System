package api.example.hrm_system.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/registered")
    @PreAuthorize("hasAnyRole('ROLE_HR', 'ROLE_MANAGER')")
    public ResponseEntity<?> getAllRegisteredUsers() {
        return userService.getAllRegisteredUsers();
    }

    @GetMapping("/verified")
    @PreAuthorize("hasAnyRole('ROLE_HR', 'ROLE_MANAGER')")
    public ResponseEntity<?> getAllVerifiedUsers() {
        return userService.getAllVerifiedUsers();
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ROLE_HR', 'ROLE_MANAGER')")
    public ResponseEntity<?> getUserStatistics() {
        return userService.getUserStatistics();
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasAnyRole('ROLE_HR', 'ROLE_MANAGER')")
    public ResponseEntity<?> getUsersByRole(@PathVariable String role) {
        return userService.getUsersByRole(role);
    }
}