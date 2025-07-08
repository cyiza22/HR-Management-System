package api.example.hrm_system.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeId(String employeeId);
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByUsername(String username);
    Optional<Employee> findBySlackId(String slackId);
    Optional<Employee> findByGithubId(String githubId);
    Optional<Employee> findBySkypeId(String skypeId);
    Optional<Employee> findByFirstNameAndLastName(String firstName, String lastName);
    boolean existsByEmail(String email);
    boolean existsByEmployeeId(String employeeId);
    boolean existsByUsername(String username);
}