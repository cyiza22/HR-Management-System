package api.example.hrm_system.employee.AccountAccess;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    @GetMapping("/my-account")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER', 'ROLE_HR')")
    public ResponseEntity<AccountAccessDTO> getMyAccountAccess(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<AccountAccessDTO> accountAccess = accountAccessService.getAccountAccessByEmail(userDetails.getUsername());
        return accountAccess.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/my-account")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER', 'ROLE_HR')")
    public ResponseEntity<AccountAccessDTO> updateMyAccountAccess(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AccountAccessDTO accountAccessDTO) {
        Optional<AccountAccessDTO> updatedAccountAccess = accountAccessService.updateAccountAccessByEmail(
                userDetails.getUsername(), accountAccessDTO);
        return updatedAccountAccess.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_HR')")
    public ResponseEntity<List<AccountAccessDTO>> getAllAccountAccess() {
        List<AccountAccessDTO> accountAccessList = accountAccessService.getAllAccountAccess();
        return ResponseEntity.ok(accountAccessList);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_HR')")
    public ResponseEntity<AccountAccessDTO> getAccountAccessById(@PathVariable Long id) {
        Optional<AccountAccessDTO> accountAccess = accountAccessService.getAccountAccessById(id);
        return accountAccess.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_HR')")
    public ResponseEntity<AccountAccessDTO> getAccountAccessByEmail(@PathVariable String email) {
        Optional<AccountAccessDTO> accountAccess = accountAccessService.getAccountAccessByEmail(email);
        return accountAccess.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/github/{githubId}")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_HR')")
    public ResponseEntity<AccountAccessDTO> getAccountAccessByGithubId(@PathVariable String githubId) {
        Optional<AccountAccessDTO> accountAccess = accountAccessService.getAccountAccessByGithubId(githubId);
        return accountAccess.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_HR')")
    public ResponseEntity<AccountAccessDTO> createAccountAccess(@Valid @RequestBody AccountAccessDTO accountAccessDTO) {
        try {
            AccountAccessDTO createdAccountAccess = accountAccessService.createAccountAccess(accountAccessDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccountAccess);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_HR')")
    public ResponseEntity<AccountAccessDTO> updateAccountAccess(@PathVariable Long id,
                                                                @Valid @RequestBody AccountAccessDTO accountAccessDTO) {
        Optional<AccountAccessDTO> updatedAccountAccess = accountAccessService.updateAccountAccess(id, accountAccessDTO);
        return updatedAccountAccess.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_HR')")
    public ResponseEntity<Void> deleteAccountAccess(@PathVariable Long id) {
        boolean deleted = accountAccessService.deleteAccountAccess(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/exists/email/{email}")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_HR')")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        boolean exists = accountAccessService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/github/{githubId}")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_HR')")
    public ResponseEntity<Boolean> existsByGithubId(@PathVariable String githubId) {
        boolean exists = accountAccessService.existsByGithubId(githubId);
        return ResponseEntity.ok(exists);
    }
}