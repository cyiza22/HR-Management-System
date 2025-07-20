package api.example.hrm_system.employee.ProfessionalInfo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/professional-info")
public class ProfessionalInfoController {

    private final ProfessionalInfoService professionalInfoService;

    public ProfessionalInfoController(ProfessionalInfoService professionalInfoService) {
        this.professionalInfoService = professionalInfoService;
    }

    @GetMapping("/my-info")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'MANAGER', 'HR')")
    public ResponseEntity<ProfessionalInfoDTO> getMyProfessionalInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return professionalInfoService.getProfessionalInfoByEmail(userDetails.getUsername())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<List<ProfessionalInfoDTO>> getAllProfessionalInfo() {
        return ResponseEntity.ok(professionalInfoService.getAllProfessionalInfo());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<ProfessionalInfoDTO> getById(@PathVariable Long id) {
        return professionalInfoService.getProfessionalInfoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<ProfessionalInfoDTO> getByEmployeeId(@PathVariable String employeeId) {
        return professionalInfoService.getProfessionalInfoByEmployeeId(employeeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<ProfessionalInfoDTO> getByUsername(@PathVariable String username) {
        return professionalInfoService.getProfessionalInfoByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<ProfessionalInfoDTO> getByEmail(@PathVariable String email) {
        return professionalInfoService.getProfessionalInfoByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/department/{department}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<List<ProfessionalInfoDTO>> getByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(professionalInfoService.getProfessionalInfoByDepartment(department));
    }

    @GetMapping("/designation/{designation}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<List<ProfessionalInfoDTO>> getByDesignation(@PathVariable String designation) {
        return ResponseEntity.ok(professionalInfoService.getProfessionalInfoByDesignation(designation));
    }

    @GetMapping("/employee-type/{employeeType}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<List<ProfessionalInfoDTO>> getByEmployeeType(@PathVariable String employeeType) {
        return ResponseEntity.ok(professionalInfoService.getProfessionalInfoByEmployeeType(employeeType));
    }

    @GetMapping("/office-location/{officeLocation}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<List<ProfessionalInfoDTO>> getByOfficeLocation(@PathVariable String officeLocation) {
        return ResponseEntity.ok(professionalInfoService.getProfessionalInfoByOfficeLocation(officeLocation));
    }

    @GetMapping("/working-days/{workingDays}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<List<ProfessionalInfoDTO>> getByWorkingDays(@PathVariable Integer workingDays) {
        return ResponseEntity.ok(professionalInfoService.getProfessionalInfoByWorkingDays(workingDays));
    }

    @GetMapping("/department/{department}/designation/{designation}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<List<ProfessionalInfoDTO>> getByDepartmentAndDesignation(
            @PathVariable String department, @PathVariable String designation) {
        return ResponseEntity.ok(professionalInfoService.getProfessionalInfoByDepartmentAndDesignation(department, designation));
    }

    @GetMapping("/department/{department}/office-location/{officeLocation}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<List<ProfessionalInfoDTO>> getByDepartmentAndOfficeLocation(
            @PathVariable String department, @PathVariable String officeLocation) {
        return ResponseEntity.ok(professionalInfoService.getProfessionalInfoByDepartmentAndOfficeLocation(department, officeLocation));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('HR')")
    public ResponseEntity<ProfessionalInfoDTO> create(@Valid @RequestBody ProfessionalInfoDTO professionalInfoDTO) {
        try {
            ProfessionalInfoDTO created = professionalInfoService.createProfessionalInfo(professionalInfoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('HR')")
    public ResponseEntity<ProfessionalInfoDTO> update(
            @PathVariable Long id, @Valid @RequestBody ProfessionalInfoDTO professionalInfoDTO) {
        try {
            return professionalInfoService.updateProfessionalInfo(id, professionalInfoDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('HR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = professionalInfoService.deleteProfessionalInfo(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/exists/employee/{employeeId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<Boolean> existsByEmployeeId(@PathVariable String employeeId) {
        return ResponseEntity.ok(professionalInfoService.existsByEmployeeId(employeeId));
    }

    @GetMapping("/exists/username/{username}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<Boolean> existsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(professionalInfoService.existsByUsername(username));
    }

    @GetMapping("/exists/email/{email}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(professionalInfoService.existsByEmail(email));
    }

    @GetMapping("/count/department/{department}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<Long> countByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(professionalInfoService.countByDepartment(department));
    }

    @GetMapping("/count/designation/{designation}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<Long> countByDesignation(@PathVariable String designation) {
        return ResponseEntity.ok(professionalInfoService.countByDesignation(designation));
    }

    @GetMapping("/count/office-location/{officeLocation}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<Long> countByOfficeLocation(@PathVariable String officeLocation) {
        return ResponseEntity.ok(professionalInfoService.countByOfficeLocation(officeLocation));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'HR')")
    public ResponseEntity<List<ProfessionalInfoDTO>> search(
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