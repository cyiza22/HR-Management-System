package api.example.hrm_system.payroll;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;



    @PostMapping
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<?> createPayroll(@Valid @RequestBody PayrollDTO dto) {
        try {
            PayrollDTO created = payrollService.createPayroll(dto);
            log.info("Payroll created successfully for employee: {}", dto.getEmployeeId());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            log.error("Error creating payroll: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Failed to create payroll: " + e.getMessage()));
        }
    }


    @GetMapping
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<List<PayrollDTO>> getAllPayrolls() {
        List<PayrollDTO> payrolls = payrollService.getAllPayrolls();
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR', 'MANAGER')")
    public ResponseEntity<?> getPayrollById(@PathVariable Long id) {
        try {
            PayrollDTO payroll = payrollService.getPayrollById(id);
            return ResponseEntity.ok(payroll);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/my-payrolls")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<PayrollDTO>> getMyPayrolls(@AuthenticationPrincipal UserDetails userDetails) {
        List<PayrollDTO> payrolls = payrollService.getPayrollByEmployeeEmail(userDetails.getUsername());
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('HR', 'MANAGER')")
    public ResponseEntity<List<PayrollDTO>> getPayrollsByEmployee(@PathVariable Long employeeId) {
        List<PayrollDTO> payrolls = payrollService.getPayrollsByEmployeeId(employeeId);
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<List<PayrollDTO>> getDepartmentPayrolls(@PathVariable Long departmentId) {
        List<PayrollDTO> payrolls = payrollService.getPayrollByDepartment(departmentId);
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('HR', 'MANAGER')")
    public ResponseEntity<List<PayrollDTO>> getPayrollsByStatus(@PathVariable Payroll.PayrollStatus status) {
        List<PayrollDTO> payrolls = payrollService.getPayrollsByStatus(status);
        return ResponseEntity.ok(payrolls);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<?> updatePayroll(
            @PathVariable Long id,
            @Valid @RequestBody PayrollDTO dto) {
        try {
            PayrollDTO updated = payrollService.updatePayroll(id, dto);
            log.info("Payroll updated successfully: {}", id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error updating payroll {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Failed to update payroll: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<?> updatePayrollStatus(
            @PathVariable Long id,
            @RequestParam Payroll.PayrollStatus status) {
        try {
            PayrollDTO updated = payrollService.updatePayrollStatus(id, status);
            log.info("Payroll status updated to {} for ID: {}", status, id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error updating payroll status: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Failed to update status: " + e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<?> deletePayroll(@PathVariable Long id) {
        try {
            payrollService.deletePayroll(id);
            log.info("Payroll deleted successfully: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting payroll {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Failed to delete payroll: " + e.getMessage()));
        }
    }


    @GetMapping("/export/pdf")
    @PreAuthorize("hasAnyRole('HR', 'MANAGER')")
    public ResponseEntity<byte[]> exportPayrollToPdf() {
        try {
            List<PayrollDTO> payrolls = payrollService.getAllPayrolls();
            byte[] pdfBytes = payrollService.generatePayrollPdf(payrolls);

            String filename = "payroll-report-" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")) + ".pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + filename);
            headers.setContentType(MediaType.APPLICATION_PDF);

            log.info("PDF export successful for payroll report");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            log.error("Error exporting payroll to PDF: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/export/pdf/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('HR', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<byte[]> exportEmployeePayrollToPdf(@PathVariable Long employeeId) {
        try {
            List<PayrollDTO> payrolls = payrollService.getPayrollsByEmployeeId(employeeId);
            byte[] pdfBytes = payrollService.generatePayrollPdf(payrolls);

            String filename = "employee-" + employeeId + "-payroll-" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + filename);
            headers.setContentType(MediaType.APPLICATION_PDF);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            log.error("Error exporting employee payroll to PDF: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}