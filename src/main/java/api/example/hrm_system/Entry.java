package api.example.hrm_system;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Entry {
    @GetMapping
    public String entry() {
        return "<h1>Welcome to HR_Management System Backend API</h1>" +
                "<p>See the <a href=''>API Documentation</a></p>";
    }
}