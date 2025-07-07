package api.example.hrm_system.Document;

import api.example.hrm_system.employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final EmployeeRepository employeeRepository;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public DocumentService(DocumentRepository documentRepository,
                           EmployeeRepository employeeRepository,
                           CloudinaryService cloudinaryService) {
        this.documentRepository = documentRepository;
        this.employeeRepository = employeeRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public Document createDocument(DocumentDTO dto) {
        // ✅ Use dto.getOwner() properly on an instance, not the class
        Employee employee = employeeRepository.findById(dto.getOwner().getId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Document doc = new Document();
        doc.setName(dto.getName());
        doc.setUrl(dto.getUrl());
        doc.setOwner(employee);
        doc.setUploadedAt(LocalDateTime.now());

        return documentRepository.save(doc);
    }

    public Document uploadDocument(MultipartFile file, String name, Long employeeId) throws IOException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        String url = cloudinaryService.uploadFile(file);

        Document doc = new Document();
        doc.setName(name);
        doc.setOwner(employee);
        doc.setUrl(url);
        doc.setUploadedAt(LocalDateTime.now());

        return documentRepository.save(doc);
    }

    public Document findById(Integer id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    public List<Document> getDocumentsByEmployeeId(Long employeeId) {
        return documentRepository.findByOwnerId(employeeId);
    }

    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    public Document updateDocument(Document updatedDoc, Integer id) {
        Document existing = findById(id);

        existing.setName(updatedDoc.getName());
        existing.setUrl(updatedDoc.getUrl());

        // Optional: update owner if needed
        if (updatedDoc.getOwner() != null && updatedDoc.getOwner().getId() != null) {
            Employee employee = employeeRepository.findById(updatedDoc.getOwner().getId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            existing.setOwner(employee);
        }

        existing.setUploadedAt(LocalDateTime.now());

        return documentRepository.save(existing);
    }

    public void deleteDocument(Integer id) {
        documentRepository.deleteById(id);
    }
}
