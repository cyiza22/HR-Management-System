package api.example.hrm_system.Candidate;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    // HR-only endpoints
    @PostMapping
    @PreAuthorize("hasAuthority('HR')")
    public ResponseEntity<Candidate> createCandidate(@RequestBody CandidateDTO dto) {
        return ResponseEntity.ok(candidateService.create(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('HR')")
    public ResponseEntity<Candidate> updateCandidate(
            @PathVariable Long id,
            @RequestBody CandidateDTO dto) {
        return ResponseEntity.ok(candidateService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('HR')")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        candidateService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // HR and Manager endpoints
    @GetMapping
    @PreAuthorize("hasAnyAuthority('HR', 'MANAGER')")
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        return ResponseEntity.ok(candidateService.findAll());
    }
}