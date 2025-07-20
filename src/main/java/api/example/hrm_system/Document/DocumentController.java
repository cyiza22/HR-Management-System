package api.example.hrm_system.Document;

import api.example.hrm_system.employee.ProfessionalInfo.ProfessionalInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/document")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'MANAGER', 'HR')")
    public ResponseEntity<Document> createDocument(@RequestBody DocumentDTO dto) {
        return ResponseEntity.ok(documentService.createDocument(dto));
    }

    @GetMapping("/getall")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(documentService.findAll());
    }

    @GetMapping("/id")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'MANAGER', 'HR')")
    public ResponseEntity<Document> getDocumentById(@RequestParam Integer id) {
        return ResponseEntity.ok(documentService.findById(id));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'MANAGER', 'HR')")
    public ResponseEntity<Document> updateDocument(@RequestBody Document document, @RequestParam Integer id) {
        Document updated = documentService.updateDocument(document, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'MANAGER', 'HR')")
    public ResponseEntity<Void> deleteDocument(@RequestParam Integer id) throws IOException {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'MANAGER', 'HR')")
    public ResponseEntity<Document> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("employeeId") Long employeeId
    ) throws IOException {
        Document uploaded = documentService.uploadDocument(file, name, employeeId);
        return ResponseEntity.ok(uploaded);
    }
}