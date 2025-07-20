package api.example.hrm_system.Config;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @GetMapping("/auth-status")
    public ResponseEntity<?> getAuthStatus() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            return ResponseEntity.ok(Map.of(
                    "authenticated", false,
                    "message", "No authentication found"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "authenticated", auth.isAuthenticated(),
                "principal", auth.getPrincipal().toString(),
                "authorities", auth.getAuthorities().toString(),
                "name", auth.getName()
        ));
    }

    @GetMapping("/employee-test")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'MANAGER', 'HR')")
    public ResponseEntity<?> employeeTest(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(Map.of(
                "message", "Employee endpoint working!",
                "user", userDetails.getUsername(),
                "authorities", userDetails.getAuthorities().toString()
        ));
    }

    @GetMapping("/manager-test")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<?> managerTest(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(Map.of(
                "message", "Manager endpoint working!",
                "user", userDetails.getUsername(),
                "authorities", userDetails.getAuthorities().toString()
        ));
    }

    @GetMapping("/hr-test")
    @PreAuthorize("hasAuthority('HR')")
    public ResponseEntity<?> hrTest(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(Map.of(
                "message", "HR endpoint working!",
                "user", userDetails.getUsername(),
                "authorities", userDetails.getAuthorities().toString()
        ));
    }
}