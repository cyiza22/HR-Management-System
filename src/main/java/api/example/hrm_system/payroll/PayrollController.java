package api.example.hrm_system.payroll;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @PostMapping("/create")
    public ResponseEntity<PayrollDTO> createPayroll(@RequestBody PayrollDTO dto) {
        return ResponseEntity.ok(payrollService.createPayroll(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayrollDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(payrollService.getPayrollById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PayrollDTO>> getAll() {
        return ResponseEntity.ok(payrollService.getAllPayrolls());
    }

    @GetMapping("/status")
    public ResponseEntity<List<PayrollDTO>> getByStatus(@RequestParam Payroll.PayrollStatus status) {
        return ResponseEntity.ok(payrollService.getByStatus(status));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PayrollDTO>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(payrollService.searchByEmployeeName(name));
    }

    @GetMapping("/deductions")
    public ResponseEntity<List<PayrollDTO>> getWithDeductions() {
        return ResponseEntity.ok(payrollService.getWithDeductions());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countByStatus(@RequestParam Payroll.PayrollStatus status) {
        return ResponseEntity.ok(payrollService.countByStatus(status));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PayrollDTO> update(@PathVariable Long id, @RequestBody PayrollDTO dto) {
        return ResponseEntity.ok(payrollService.updatePayroll(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        payrollService.deletePayroll(id);
        return ResponseEntity.noContent().build();
    }
}

