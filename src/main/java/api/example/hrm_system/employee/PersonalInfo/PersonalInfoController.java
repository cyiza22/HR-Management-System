package api.example.hrm_system.employee.PersonalInfo;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personal-info")
@CrossOrigin(origins = "*")
public class PersonalInfoController {

    private PersonalInfoService personalInfoService;

    @PostMapping
    public ResponseEntity<PersonalInfoDTO> createPersonalInfo(@RequestBody PersonalInfoDTO personalInfoDto) {
        try {
            PersonalInfoDTO savedPersonalInfo = personalInfoService.savePersonalInfo(personalInfoDto);
            return new ResponseEntity<>(savedPersonalInfo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonalInfoDTO> updatePersonalInfo(@PathVariable Long id, @RequestBody PersonalInfoDTO personalInfoDto) {
        try {
            PersonalInfoDTO updatedPersonalInfo = personalInfoService.updatePersonalInfo(id, personalInfoDto);
            return new ResponseEntity<>(updatedPersonalInfo, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonalInfoDTO> getPersonalInfoById(@PathVariable Long id) {
        try {
            PersonalInfoDTO personalInfo = personalInfoService.getPersonalInfoById(id);
            return new ResponseEntity<>(personalInfo, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/name/{firstName}/{lastName}")
    public ResponseEntity<PersonalInfoDTO> getPersonalInfoByName(@PathVariable String firstName, @PathVariable String lastName) {
        try {
            PersonalInfoDTO personalInfo = personalInfoService.getPersonalInfoByName(firstName, lastName);
            return new ResponseEntity<>(personalInfo, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search/firstname/{firstName}")
    public ResponseEntity<List<PersonalInfoDTO>> searchByFirstName(@PathVariable String firstName) {
        try {
            List<PersonalInfoDTO> personalInfoList = personalInfoService.searchByFirstName(firstName);
            return new ResponseEntity<>(personalInfoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/lastname/{lastName}")
    public ResponseEntity<List<PersonalInfoDTO>> searchByLastName(@PathVariable String lastName) {
        try {
            List<PersonalInfoDTO> personalInfoList = personalInfoService.searchByLastName(lastName);
            return new ResponseEntity<>(personalInfoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/fullname/{fullName}")
    public ResponseEntity<List<PersonalInfoDTO>> searchByFullName(@PathVariable String fullName) {
        try {
            List<PersonalInfoDTO> personalInfoList = personalInfoService.searchByFullName(fullName);
            return new ResponseEntity<>(personalInfoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<PersonalInfoDTO> getPersonalInfoByEmail(@PathVariable String email) {
        try {
            PersonalInfoDTO personalInfo = personalInfoService.getPersonalInfoByEmail(email);
            return new ResponseEntity<>(personalInfo, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<PersonalInfoDTO>> getAllPersonalInfo() {
        try {
            List<PersonalInfoDTO> personalInfoList = personalInfoService.getAllPersonalInfo();
            return new ResponseEntity<>(personalInfoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePersonalInfo(@PathVariable Long id) {
        try {
            personalInfoService.deletePersonalInfo(id);
            return new ResponseEntity<>("Personal information deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting personal information", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
