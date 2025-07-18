package api.example.hrm_system.Document;

import api.example.hrm_system.employee.ProfessionalInfo.ProfessionalInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/document") // Correct mapping annotation
public class DocumentController {

    private final DocumentService documentService;
    private final ProfessionalInfoService professionalInfoService;

    public DocumentController(DocumentService documentService, ProfessionalInfoService professionalInfoService) {
        this.documentService = documentService;
        this.professionalInfoService = professionalInfoService;
    }

    @PostMapping("/new")
    public ResponseEntity<Document> createDocument(@RequestBody DocumentDTO dto) {
        return ResponseEntity.ok(documentService.createDocument(dto));
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(documentService.findAll());
    }

    @GetMapping("/id")
    public ResponseEntity<Document> getDocumentById(@RequestParam Integer id) {
        return ResponseEntity.ok(documentService.findById(id));
    }

    @PutMapping("/update")
    public ResponseEntity<Document> updateDocument(@RequestBody Document document, @RequestParam Integer id) {
        Document updated = documentService.updateDocument(document, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteDocument(@RequestParam Integer id) throws IOException {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("employeeId") Long employeeId
    ) throws IOException {
        Document uploaded = documentService.uploadDocument(file, name, employeeId);
        return ResponseEntity.ok(uploaded);
    }
}
