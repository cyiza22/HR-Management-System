package api.example.hrm_system.employee.AccountAccess;

import api.example.hrm_system.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AccountAccessRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findBySlackId(String slackId);

    Optional<Employee> findByGithubId(String githubId);

    Optional<Employee> findBySkypeId(String skypeId);

    boolean existsByEmail(String email);

    boolean existsBySlackId(String slackId);

    boolean existsByGithubId(String githubId);

    boolean existsBySkypeId(String skypeId);
}
