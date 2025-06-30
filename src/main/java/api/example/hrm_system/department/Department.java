package api.example.hrm_system.department;

import api.example.hrm_system.employee.Employee;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // department_id

    @Column(unique = true)
    private String departmentName;

    private String location;

    @Enumerated(EnumType.STRING)
    private WorkType workType;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;

    // Manager of the department (optional, one employee can manage a department)
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Employees under this department
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Employee> employees;

    public enum WorkType {
        Office,
        Remote,
        Hybrid
    }

    public enum EmploymentStatus {
        Active,
        Inactive,
        Suspended,
        Terminated
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}