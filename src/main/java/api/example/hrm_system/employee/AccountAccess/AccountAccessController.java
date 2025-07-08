package api.example.hrm_system.employee.AccountAccess;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/account-access")
public class AccountAccessController {

    @Autowired
    private AccountAccessService accountAccessService;
    public AccountAccessController(AccountAccessService accountAccessService) {
        this.accountAccessService = accountAccessService;
    }

    @GetMapping
    public ResponseEntity<List<AccountAccessDTO>> getAllAccountAccess() {
        List<AccountAccessDTO> accountAccessList = accountAccessService.getAllAccountAccess();
        return ResponseEntity.ok(accountAccessList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountAccessDTO> getAccountAccessById(@PathVariable Long id) {
        Optional<AccountAccessDTO> accountAccess = accountAccessService.getAccountAccessById(id);
        return accountAccess.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<AccountAccessDTO> getAccountAccessByEmail(@PathVariable String email) {
        Optional<AccountAccessDTO> accountAccess = accountAccessService.getAccountAccessByEmail(email);
        return accountAccess.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/slack/{slackId}")
    public ResponseEntity<AccountAccessDTO> getAccountAccessBySlackId(@PathVariable String slackId) {
        Optional<AccountAccessDTO> accountAccess = accountAccessService.getAccountAccessBySlackId(slackId);
        return accountAccess.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/github/{githubId}")
    public ResponseEntity<AccountAccessDTO> getAccountAccessByGithubId(@PathVariable String githubId) {
        Optional<AccountAccessDTO> accountAccess = accountAccessService.getAccountAccessByGithubId(githubId);
        return accountAccess.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/skype/{skypeId}")
    public ResponseEntity<AccountAccessDTO> getAccountAccessBySkypeId(@PathVariable String skypeId) {
        Optional<AccountAccessDTO> accountAccess = accountAccessService.getAccountAccessBySkypeId(skypeId);
        return accountAccess.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AccountAccessDTO> createAccountAccess(@Valid @RequestBody AccountAccessDTO accountAccessDTO) {
        try {
            AccountAccessDTO createdAccountAccess = accountAccessService.createAccountAccess(accountAccessDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccountAccess);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountAccessDTO> updateAccountAccess(@PathVariable Long id,
                                                                @Valid @RequestBody AccountAccessDTO accountAccessDTO) {
        Optional<AccountAccessDTO> updatedAccountAccess = accountAccessService.updateAccountAccess(id, accountAccessDTO);
        return updatedAccountAccess.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountAccess(@PathVariable Long id) {
        boolean deleted = accountAccessService.deleteAccountAccess(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        boolean exists = accountAccessService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/slack/{slackId}")
    public ResponseEntity<Boolean> existsBySlackId(@PathVariable String slackId) {
        boolean exists = accountAccessService.existsBySlackId(slackId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/github/{githubId}")
    public ResponseEntity<Boolean> existsByGithubId(@PathVariable String githubId) {
        boolean exists = accountAccessService.existsByGithubId(githubId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/skype/{skypeId}")
    public ResponseEntity<Boolean> existsBySkypeId(@PathVariable String skypeId) {
        boolean exists = accountAccessService.existsBySkypeId(skypeId);
        return ResponseEntity.ok(exists);
    }
}