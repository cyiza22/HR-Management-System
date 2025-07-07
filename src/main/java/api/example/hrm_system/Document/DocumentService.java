package api.example.hrm_system.Document;

import api.example.hrm_system.employee.Employee;
import api.example.hrm_system.Cloudinary.CloudinaryService;
import api.example.hrm_system.employee.ProfessionalInfo.ProfessionalInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final ProfessionalInfoRepository professionalInfoRepository;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public DocumentService(DocumentRepository documentRepository,
                           ProfessionalInfoRepository professionalInfoRepository,
                           CloudinaryService cloudinaryService) {
        this.documentRepository = documentRepository;
        this.professionalInfoRepository = professionalInfoRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public Document createDocument(DocumentDTO dto) {
        Employee employee = professionalInfoRepository.findById(dto.getOwner().getId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Document doc = new Document();
        doc.setName(dto.getName());
        doc.setUrl(dto.getUrl());
        doc.setOwner(employee);
        doc.setUploadedAt(LocalDateTime.now());

        return documentRepository.save(doc);
    }

    public Document uploadDocument(MultipartFile file, String name, Long employeeId) throws IOException {
        Employee employee = professionalInfoRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));


        Map<String, String> cloudinaryData = cloudinaryService.uploadFile(file);

        Document document = new Document();
        document.setUrl(cloudinaryData.get("url"));
        document.setPublicId(cloudinaryData.get("publicId"));
        document.setName(file.getName());
        document.setUploadedAt(LocalDateTime.now());
        document.setOwner(employee);

        documentRepository.save(document);

        return document;
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
        if (updatedDoc.getOwner() != null && updatedDoc.getOwner().getId() != null) {
            Employee employee = professionalInfoRepository.findById(updatedDoc.getOwner().getId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            existing.setOwner(employee);
        }

        existing.setUploadedAt(LocalDateTime.now());

        return documentRepository.save(existing);
    }

    public void deleteDocument(Integer documentId) throws IOException {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        cloudinaryService.deleteFile(document.getPublicId());

        documentRepository.delete(document);
    }

}
