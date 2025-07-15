package api.example.hrm_system.user;

import api.example.hrm_system.DTOs.UserDTO;
import api.example.hrm_system.DTOs.UserStatsDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new UserPrincipal(user);
    }

    public ResponseEntity<?> getAllRegisteredUsers() {
        try {
            List<User> users = userRepository.findAll();
            List<UserDTO> userDtos = users.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            logger.info("Retrieved {} registered users", userDtos.size());

            return ResponseEntity.ok()
                    .body(Map.of(
                            "message", "Users retrieved successfully",
                            "totalUsers", userDtos.size(),
                            "users", userDtos
                    ));
        } catch (Exception e) {
            logger.error("Failed to retrieve registered users", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to retrieve users: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> getAllVerifiedUsers() {
        try {
            List<User> verifiedUsers = userRepository.findByVerifiedTrue();
            List<UserDTO> userDtos = verifiedUsers.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            logger.info("Retrieved {} verified users", userDtos.size());

            return ResponseEntity.ok()
                    .body(Map.of(
                            "message", "Verified users retrieved successfully",
                            "totalVerifiedUsers", userDtos.size(),
                            "users", userDtos
                    ));
        } catch (Exception e) {
            logger.error("Failed to retrieve verified users", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to retrieve verified users: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> getUserStatistics() {
        try {
            long totalUsers = userRepository.count();
            long verifiedUsers = userRepository.countByVerifiedTrue();
            long unverifiedUsers = userRepository.countByVerifiedFalse();

            // Count by roles
            long employees = userRepository.countByRole(Role.EMPLOYEE);
            long managers = userRepository.countByRole(Role.MANAGER);
            long hrUsers = userRepository.countByRole(Role.HR);

            UserStatsDto stats = new UserStatsDto();
            stats.setTotalUsers(totalUsers);
            stats.setVerifiedUsers(verifiedUsers);
            stats.setUnverifiedUsers(unverifiedUsers);
            stats.setEmployees(employees);
            stats.setManagers(managers);
            stats.setHrUsers(hrUsers);

            logger.info("User statistics retrieved successfully");

            return ResponseEntity.ok()
                    .body(Map.of(
                            "message", "User statistics retrieved successfully",
                            "statistics", stats
                    ));
        } catch (Exception e) {
            logger.error("Failed to retrieve user statistics", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to retrieve statistics: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> getUsersByRole(String roleString) {
        try {
            if (!Role.isValidRole(roleString)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid role. Valid roles are: EMPLOYEE, MANAGER, HR"));
            }

            Role role = Role.fromString(roleString);
            List<User> users = userRepository.findByRole(role);
            List<UserDTO> userDtos = users.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            logger.info("Retrieved {} users with role {}", userDtos.size(), role);

            return ResponseEntity.ok()
                    .body(Map.of(
                            "message", "Users with role " + role + " retrieved successfully",
                            "role", role.name(),
                            "totalUsers", userDtos.size(),
                            "users", userDtos
                    ));
        } catch (Exception e) {
            logger.error("Failed to retrieve users by role: {}", roleString, e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to retrieve users by role: " + e.getMessage()));
        }
    }

    private UserDTO convertToDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setVerified(user.isVerified());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}