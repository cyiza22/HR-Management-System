package api.example.hrm_system.employee.ProfessionalInfo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/professional-info")
@CrossOrigin(origins = "*")
public class ProfessionalInfoController {

    private ProfessionalInfoService professionalInfoService;


    @GetMapping("/getAll")
    public ResponseEntity<List<ProfessionalInfoDTO>> getAllProfessionalInfo() {
        List<ProfessionalInfoDTO> professionalInfoList = professionalInfoService.getAllProfessionalInfo();
        return ResponseEntity.ok(professionalInfoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalInfoDTO> getProfessionalInfoById(@PathVariable Long id) {
        Optional<ProfessionalInfoDTO> professionalInfo = professionalInfoService.getProfessionalInfoById(id);
        return professionalInfo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ProfessionalInfoDTO> getProfessionalInfoByEmployeeId(@PathVariable String employeeId) {
        Optional<ProfessionalInfoDTO> professionalInfo = professionalInfoService.getProfessionalInfoByEmployeeId(employeeId);
        return professionalInfo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/username/{username}")
    public ResponseEntity<ProfessionalInfoDTO> getProfessionalInfoByUsername(@PathVariable String username) {
        Optional<ProfessionalInfoDTO> professionalInfo = professionalInfoService.getProfessionalInfoByUsername(username);
        return professionalInfo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<ProfessionalInfoDTO> getProfessionalInfoByEmail(@PathVariable String email) {
        Optional<ProfessionalInfoDTO> professionalInfo = professionalInfoService.getProfessionalInfoByEmail(email);
        return professionalInfo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<ProfessionalInfoDTO>> getProfessionalInfoByDepartment(@PathVariable String department) {
        List<ProfessionalInfoDTO> professionalInfoList = professionalInfoService.getProfessionalInfoByDepartment(department);
        return ResponseEntity.ok(professionalInfoList);
    }


    @GetMapping("/designation/{designation}")
    public ResponseEntity<List<ProfessionalInfoDTO>> getProfessionalInfoByDesignation(@PathVariable String designation) {
        List<ProfessionalInfoDTO> professionalInfoList = professionalInfoService.getProfessionalInfoByDesignation(designation);
        return ResponseEntity.ok(professionalInfoList);
    }

    @GetMapping("/employee-type/{employeeType}")
    public ResponseEntity<List<ProfessionalInfoDTO>> getProfessionalInfoByEmployeeType(@PathVariable String employeeType) {
        List<ProfessionalInfoDTO> professionalInfoList = professionalInfoService.getProfessionalInfoByEmployeeType(employeeType);
        return ResponseEntity.ok(professionalInfoList);
    }


    @GetMapping("/office-location/{officeLocation}")
    public ResponseEntity<List<ProfessionalInfoDTO>> getProfessionalInfoByOfficeLocation(@PathVariable String officeLocation) {
        List<ProfessionalInfoDTO> professionalInfoList = professionalInfoService.getProfessionalInfoByOfficeLocation(officeLocation);
        return ResponseEntity.ok(professionalInfoList);
    }

    @GetMapping("/working-days/{workingDays}")
    public ResponseEntity<List<ProfessionalInfoDTO>> getProfessionalInfoByWorkingDays(@PathVariable String workingDays) {
        List<ProfessionalInfoDTO> professionalInfoList = professionalInfoService.getProfessionalInfoByWorkingDays(workingDays);
        return ResponseEntity.ok(professionalInfoList);
    }

    @GetMapping("/department/{department}/designation/{designation}")
    public ResponseEntity<List<ProfessionalInfoDTO>> getProfessionalInfoByDepartmentAndDesignation(
            @PathVariable String department, @PathVariable String designation) {
        List<ProfessionalInfoDTO> professionalInfoList =
                professionalInfoService.getProfessionalInfoByDepartmentAndDesignation(department, designation);
        return ResponseEntity.ok(professionalInfoList);
    }

    @GetMapping("/department/{department}/office-location/{officeLocation}")
    public ResponseEntity<List<ProfessionalInfoDTO>> getProfessionalInfoByDepartmentAndOfficeLocation(
            @PathVariable String department, @PathVariable String officeLocation) {
        List<ProfessionalInfoDTO> professionalInfoList =
                professionalInfoService.getProfessionalInfoByDepartmentAndOfficeLocation(department, officeLocation);
        return ResponseEntity.ok(professionalInfoList);
    }

    @PostMapping
    public ResponseEntity<ProfessionalInfoDTO> createProfessionalInfo(@Valid @RequestBody ProfessionalInfoDTO professionalInfoDTO) {
        try {
            ProfessionalInfoDTO createdProfessionalInfo = professionalInfoService.createProfessionalInfo(professionalInfoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProfessionalInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalInfoDTO> updateProfessionalInfo(
            @PathVariable Long id, @Valid @RequestBody ProfessionalInfoDTO professionalInfoDTO) {
        try {
            Optional<ProfessionalInfoDTO> updatedProfessionalInfo =
                    professionalInfoService.updateProfessionalInfo(id, professionalInfoDTO);
            return updatedProfessionalInfo.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessionalInfo(@PathVariable Long id) {
        boolean deleted = professionalInfoService.deleteProfessionalInfo(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/exists/employee/{employeeId}")
    public ResponseEntity<Boolean> existsByEmployeeId(@PathVariable String employeeId) {
        boolean exists = professionalInfoService.existsByEmployeeId(employeeId);
        return ResponseEntity.ok(exists);
    }


    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> existsByUsername(@PathVariable String username) {
        boolean exists = professionalInfoService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        boolean exists = professionalInfoService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/count/department/{department}")
    public ResponseEntity<Long> countByDepartment(@PathVariable String department) {
        long count = professionalInfoService.countByDepartment(department);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/designation/{designation}")
    public ResponseEntity<Long> countByDesignation(@PathVariable String designation) {
        long count = professionalInfoService.countByDesignation(designation);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/office-location/{officeLocation}")
    public ResponseEntity<Long> countByOfficeLocation(@PathVariable String officeLocation) {
        long count = professionalInfoService.countByOfficeLocation(officeLocation);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProfessionalInfoDTO>> searchProfessionalInfo(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String designation,
            @RequestParam(required = false) String employeeType,
            @RequestParam(required = false) String officeLocation) {

        List<ProfessionalInfoDTO> results;

        if (department != null && designation != null) {
            results = professionalInfoService.getProfessionalInfoByDepartmentAndDesignation(department, designation);
        } else if (department != null && officeLocation != null) {
            results = professionalInfoService.getProfessionalInfoByDepartmentAndOfficeLocation(department, officeLocation);
        } else if (department != null) {
            results = professionalInfoService.getProfessionalInfoByDepartment(department);
        } else if (designation != null) {
            results = professionalInfoService.getProfessionalInfoByDesignation(designation);
        } else if (employeeType != null) {
            results = professionalInfoService.getProfessionalInfoByEmployeeType(employeeType);
        } else if (officeLocation != null) {
            results = professionalInfoService.getProfessionalInfoByOfficeLocation(officeLocation);
        } else {
            results = professionalInfoService.getAllProfessionalInfo();
        }

        return ResponseEntity.ok(results);
    }
}