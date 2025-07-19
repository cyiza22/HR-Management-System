package api.example.hrm_system.Config;

import api.example.hrm_system.user.Role;
import api.example.hrm_system.user.User;
import api.example.hrm_system.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitialization implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initializeHRAccount();
    }

    private void initializeHRAccount() {
        String hrEmail = "hrms.hr@gmail.com";
        String hrPassword = "HrPassword12345";

        try {
            if (!userRepository.existsByEmail(hrEmail)) {
                User hrUser = User.builder()
                        .fullName("HR Manager")
                        .email(hrEmail)
                        .password(passwordEncoder.encode(hrPassword))
                        .role(Role.HR)
                        .verified(true)
                        .build();

                userRepository.save(hrUser);
                log.info("HR account created successfully with email: {}", hrEmail);
                log.info("HR can login with email: {} and password: {}", hrEmail, hrPassword);
            } else {
                log.info("HR account already exists with email: {}", hrEmail);
            }
        } catch (Exception e) {
            log.error("Failed to initialize HR account: {}", e.getMessage());
        }
    }
}