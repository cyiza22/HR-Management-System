package api.example.hrm_system.employee.AccountAccess;

import api.example.hrm_system.employee.EmployeeRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountAccessRepository extends EmployeeRepository {
    boolean existsBySlackId(String slackId);
    boolean existsByGithubId(String githubId);
    boolean existsBySkypeId(String skypeId);
}