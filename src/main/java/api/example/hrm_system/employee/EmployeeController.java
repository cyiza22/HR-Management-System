package api.example.hrm_system.employee;

import api.example.hrm_system.employee.AccountAccess.AccountAccessService;
import api.example.hrm_system.employee.EmployeeDashboard.EmployeeDashboardDTO;
import api.example.hrm_system.employee.EmployeeDashboard.EmployeeDashboardService;
import api.example.hrm_system.employee.PersonalInfo.PersonalInfoDTO;
import api.example.hrm_system.employee.PersonalInfo.PersonalInfoService;
import api.example.hrm_system.employee.ProfessionalInfo.ProfessionalInfoDTO;
import api.example.hrm_system.employee.AccountAccess.AccountAccessDTO;
import api.example.hrm_system.employee.ProfessionalInfo.ProfessionalInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final PersonalInfoService personalInfoService;
    private final ProfessionalInfoService professionalInfoService;
    private final AccountAccessService accountAccessService;
    private final EmployeeDashboardService employeeDashboardService;

    // Manager and HR endpoints
    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<List<EmployeeDashboardDTO>> getDepartmentEmployees(@PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeDashboardService.getEmployeesByDepartment(departmentId.toString()));
    }

    // Employee-specific endpoints
    @GetMapping("/my-profile")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<EmployeeDashboardDTO> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(employeeDashboardService.getEmployeeByEmail(userDetails.getUsername()));
    }

    // Personal Info endpoints
    @GetMapping("/personal-info")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<PersonalInfoDTO> getMyPersonalInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(personalInfoService.getPersonalInfoByEmail(userDetails.getUsername()));
    }

    @PutMapping("/personal-info")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<PersonalInfoDTO> updateMyPersonalInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PersonalInfoDTO dto) {
        return ResponseEntity.ok(personalInfoService.updatePersonalInfoByEmail(userDetails.getUsername(), dto));
    }

    // Professional Info endpoints
    @GetMapping("/professional-info")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'HR')")
    public ResponseEntity<ProfessionalInfoDTO> getProfessionalInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(professionalInfoService.getProfessionalInfoByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Professional info not found")));
    }

    // Account Access endpoints
    @GetMapping("/account-access")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<AccountAccessDTO> getMyAccountAccess(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(accountAccessService.getAccountAccessByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Account access not found")));
    }

    @PutMapping("/account-access")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<AccountAccessDTO> updateMyAccountAccess(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AccountAccessDTO dto) {
        return ResponseEntity.ok(accountAccessService.updateAccountAccessByEmail(userDetails.getUsername(), dto)
                .orElseThrow(() -> new RuntimeException("Failed to update account access")));
    }

    // Dashboard endpoints
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<EmployeeDashboardDTO> getMyDashboard(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(employeeDashboardService.getEmployeeByEmail(userDetails.getUsername()));
    }
}