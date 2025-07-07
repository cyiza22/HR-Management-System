package api.example.hrm_system.employee;

import api.example.hrm_system.Document.Document;
import api.example.hrm_system.Leave.Leave;
import api.example.hrm_system.Project.Project;
import api.example.hrm_system.attendance.Attendance;
import api.example.hrm_system.department.Department;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee {
    @Id

    private Long id;

    @Column(unique = true)
    private String employeeId;

    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;


    private String mobileNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String nationality;
    private String maritalStatus;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String slackId;
    private String githubId;
    private String skypeId;
    private String username;

    private String designation;


    private String employeeType; // Office, Remote

    private LocalDate joiningDate;

    private Integer workingDays;
    private String officeLocation;
    private String status; // Permanent, Contract, etc.



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager")
    @JsonManagedReference
    private Department department;


    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Attendance> attendances;

    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Project> projects;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Leave> leaves;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Document> documents;



}
