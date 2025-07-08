package api.example.hrm_system.employee.PersonalInfo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInfoDTO {
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Pattern(regexp = "^[0-9]{10,15}$")
    private String mobileNumber;

    @Email
    private String email;

    @Past
    private LocalDate dateOfBirth;

    private String gender;
    private String nationality;
    private String maritalStatus;
    private String address;
    private String city;
    private String state;

    @Pattern(regexp = "^[0-9]{5,10}$")
    private String zipCode;
}