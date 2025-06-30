package api.example.hrm_system.Candidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidate")
public class CandidateController {

    private final CandidateService candidateService;

    @Autowired
    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping("/new")
    public ResponseEntity<Candidate> create(@RequestBody CandidateDTO dto) {
        return ResponseEntity.ok(candidateService.create(dto));
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Candidate>> findAll() {
        return ResponseEntity.ok(candidateService.findAll());
    }

    @GetMapping("/id")
    public ResponseEntity<Candidate> findById(@RequestParam Long id) {
        return ResponseEntity.ok(candidateService.findById(id));
    }

    @GetMapping("/name")
    public ResponseEntity<List<Candidate>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(candidateService.findByName(name));
    }

    @PutMapping("/update")
    public ResponseEntity<Candidate> update(@RequestBody CandidateDTO dto, @RequestParam Long id) {
        Candidate candidate = candidateService.update(dto, id);
        return new ResponseEntity<>(candidate, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        candidateService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
