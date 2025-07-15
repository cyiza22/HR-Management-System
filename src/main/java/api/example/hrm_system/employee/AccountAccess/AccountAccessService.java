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

    public Optional<AccountAccessDTO> getAccountAccessByGithubId(String githubId) {
        return accountAccessRepository.findByGithubId(githubId)
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

    public boolean existsByGithubId(String githubId) {
        return accountAccessRepository.existsByGithubId(githubId);
    }

    private AccountAccessDTO convertToDTO(Employee employee) {
        AccountAccessDTO dto = new AccountAccessDTO();
        dto.setEmail(employee.getEmail());
        dto.setLinkedIn(employee.getLinkedIn());
        dto.setGithubId(employee.getGithubId());
        dto.setBankAccountNumber(employee.getBankAccountNumber());
        return dto;
    }

    private void updateEmployeeFromDTO(Employee employee, AccountAccessDTO dto) {
        if (dto.getEmail() != null) {
            employee.setEmail(dto.getEmail());
        }
        if (dto.getLinkedIn() != null) {
            employee.setLinkedIn(dto.getLinkedIn());
        }
        if (dto.getGithubId() != null) {
            employee.setGithubId(dto.getGithubId());
        }
        if (dto.getBankAccountNumber() != null) {
            employee.setBankAccountNumber(dto.getBankAccountNumber());
        }
    }
}