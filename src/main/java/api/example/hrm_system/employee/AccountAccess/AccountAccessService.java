package api.example.hrm_system.employee.AccountAccess;

import api.example.hrm_system.employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AccountAccessService {

    @Autowired
    private AccountAccessRepository accountAccessRepository;

    public List<AccountAccessDTO> getAllAccountAccess() {
        return accountAccessRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
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
        Employee entity = convertToEntity(accountAccessDTO);
        Employee savedEntity = accountAccessRepository.save(entity);
        return convertToDTO(savedEntity);
    }

    public Optional<AccountAccessDTO> updateAccountAccess(Long id, AccountAccessDTO accountAccessDTO) {
        return accountAccessRepository.findById(id)
                .map(existingEntity -> {
                    updateEntityFromDTO(existingEntity, accountAccessDTO);
                    Employee updatedEntity = accountAccessRepository.save(existingEntity);
                    return convertToDTO(updatedEntity);
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

    private AccountAccessDTO convertToDTO(Employee entity) {
        AccountAccessDTO dto = new AccountAccessDTO();
        dto.setSlackId(entity.getSlackId());
        dto.setGithubId(entity.getGithubId());
        dto.setSkypeId(entity.getSkypeId());
        dto.setEmail(entity.getEmail());
        return dto;
    }

    private Employee convertToEntity(AccountAccessDTO dto) {
        Employee entity = new Employee();
        entity.setSlackId(dto.getSlackId());
        entity.setGithubId(dto.getGithubId());
        entity.setSkypeId(dto.getSkypeId());
        entity.setEmail(dto.getEmail());
        return entity;
    }

    private void updateEntityFromDTO(Employee entity, AccountAccessDTO dto) {
        entity.setSlackId(dto.getSlackId());
        entity.setGithubId(dto.getGithubId());
        entity.setSkypeId(dto.getSkypeId());
        entity.setEmail(dto.getEmail());
    }
}
