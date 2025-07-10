package api.example.hrm_system.payroll;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;

    @PostMapping
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<PayrollDTO> createPayroll(@Valid @RequestBody PayrollDTO dto) {
        return ResponseEntity.ok(payrollService.createPayroll(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayrollDTO> getPayrollById(@PathVariable Long id) {
        return ResponseEntity.ok(payrollService.getPayrollById(id));
    }

    @GetMapping("/my-payrolls")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<PayrollDTO>> getMyPayrolls(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(payrollService.getPayrollByEmployeeEmail(userDetails.getUsername()));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<PayrollDTO>> getDepartmentPayrolls(@PathVariable Long departmentId) {
        return ResponseEntity.ok(payrollService.getPayrollByDepartment(departmentId));
    }

    @GetMapping("/export/pdf")
    @PreAuthorize("hasAnyRole('HR', 'MANAGER')")
    public ResponseEntity<byte[]> exportPayrollToPdf() {
        byte[] pdfBytes = payrollService.exportPayrollToPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=payroll-report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<PayrollDTO> updatePayroll(
            @PathVariable Long id,
            @Valid @RequestBody PayrollDTO dto) {
        return ResponseEntity.ok(payrollService.updatePayroll(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<Void> deletePayroll(@PathVariable Long id) {
        payrollService.deletePayroll(id);
        return ResponseEntity.noContent().build();
    }
}