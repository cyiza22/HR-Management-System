package api.example.hrm_system.employee.PersonalInfo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInfoDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String email;
    private LocalDate dateOfBirth;
    private String gender;
    private String nationality;
    private String maritalStatus;
    private String address;
    private String city;
    private String state;
    private String zipCode;
}

