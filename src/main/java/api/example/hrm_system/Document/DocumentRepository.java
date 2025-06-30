package api.example.hrm_system.Document;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.net.ssl.SSLSession;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
    List<Document> findByName(String name);
    List<Document> findByOwnerId(Long employeeId);


}
