package api.example.hrm_system.employee.AccountAccess;

import api.example.hrm_system.employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountAccessService {

    @Autowired
    private AccountAccessRepository accountAccessRepository;

    public List<AccountAccessDTO> getAllAccountAccess() {
        return accountAccessRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AccountAccessDTO> getAccountAccessById(Long id) {
        return accountAccessRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<AccountAccessDTO> getAccountAccessByEmail(String email) {
        return accountAccessRepository.findByEmail(email)
                .map(this::convertToDTO);
    }

    public Optional<AccountAccessDTO> getAccountAccessBySlackId(String slackId) {
        return accountAccessRepository.findBySlackId(slackId)
                .map(this::convertToDTO);
    }

    public Optional<AccountAccessDTO> getAccountAccessByGithubId(String githubId) {
        return accountAccessRepository.findByGithubId(githubId)
                .map(this::convertToDTO);
    }

    public Optional<AccountAccessDTO> getAccountAccessBySkypeId(String skypeId) {
        return accountAccessRepository.findBySkypeId(skypeId)
                .map(this::convertToDTO);
    }

    public AccountAccessDTO createAccountAccess(AccountAccessDTO accountAccessDTO) {
        Employee employee = new Employee();
        updateEmployeeFromDTO(employee, accountAccessDTO);
        Employee savedEmployee = accountAccessRepository.save(employee);
        return convertToDTO(savedEmployee);
    }

    public Optional<AccountAccessDTO> updateAccountAccess(Long id, AccountAccessDTO accountAccessDTO) {
        return accountAccessRepository.findById(id)
                .map(existingEmployee -> {
                    updateEmployeeFromDTO(existingEmployee, accountAccessDTO);
                    Employee updatedEmployee = accountAccessRepository.save(existingEmployee);
                    return convertToDTO(updatedEmployee);
                });
    }

    public Optional<AccountAccessDTO> updateAccountAccessByEmail(String email, AccountAccessDTO dto) {
        return accountAccessRepository.findByEmail(email)
                .map(existingEmployee -> {
                    updateEmployeeFromDTO(existingEmployee, dto);
                    Employee updatedEmployee = accountAccessRepository.save(existingEmployee);
                    return convertToDTO(updatedEmployee);
                });
    }

    public boolean deleteAccountAccess(Long id) {
        if (accountAccessRepository.existsById(id)) {
            accountAccessRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean existsByEmail(String email) {
        return accountAccessRepository.existsByEmail(email);
    }

    public boolean existsBySlackId(String slackId) {
        return accountAccessRepository.existsBySlackId(slackId);
    }

    public boolean existsByGithubId(String githubId) {
        return accountAccessRepository.existsByGithubId(githubId);
    }

    public boolean existsBySkypeId(String skypeId) {
        return accountAccessRepository.existsBySkypeId(skypeId);
    }

    private AccountAccessDTO convertToDTO(Employee employee) {
        AccountAccessDTO dto = new AccountAccessDTO();
        dto.setEmployeeId(employee.getId());
        dto.setSlackId(employee.getSlackId());
        dto.setGithubId(employee.getGithubId());
        dto.setSkypeId(employee.getSkypeId());
        dto.setEmail(employee.getEmail());
        return dto;
    }

    private void updateEmployeeFromDTO(Employee employee, AccountAccessDTO dto) {
        employee.setSlackId(dto.getSlackId());
        employee.setGithubId(dto.getGithubId());
        employee.setSkypeId(dto.getSkypeId());
        employee.setEmail(dto.getEmail());
    }
}