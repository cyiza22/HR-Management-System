package api.example.hrm_system.Cloudinary;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    private final CloudinaryService cloudinaryService;

    // Constructor injection is preferred
    public FileUploadController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, String> result = cloudinaryService.uploadFile(file);
            return ResponseEntity.ok(result); // returns JSON: { "url": "...", "publicId": "..." }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }
}
