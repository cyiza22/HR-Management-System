package api.example.hrm_system.payroll;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayrollService {

    private final PayrollRepository payrollRepository;


    public PayrollService(PayrollRepository payrollRepository) {
        this.payrollRepository = payrollRepository;
    }

    public PayrollDTO createPayroll(PayrollDTO dto) {
        Payroll payroll = mapToEntity(dto);
        return mapToDTO(payrollRepository.save(payroll));
    }

    public PayrollDTO getPayrollById(Long id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found with ID: " + id));
        return mapToDTO(payroll);
    }

    public List<PayrollDTO> getAllPayrolls() {
        return payrollRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PayrollDTO> getByStatus(Payroll.PayrollStatus status) {
        return payrollRepository.findByStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PayrollDTO> searchByEmployeeName(String name) {
        return payrollRepository.findByEmployeeNameContainingIgnoreCase(name).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PayrollDTO> getWithDeductions() {
        return payrollRepository.findEmployeesWithDeductions().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public long countByStatus(Payroll.PayrollStatus status) {
        return payrollRepository.countByStatus(status);
    }

    public PayrollDTO updatePayroll(Long id, PayrollDTO dto) {
        Payroll existing = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found with ID: " + id));

        existing.setEmployeeName(dto.getEmployeeName());
        existing.setCtc(dto.getCtc());
        existing.setSalaryPerMonth(dto.getSalaryPerMonth());
        existing.setDeduction(dto.getDeduction());
        existing.setStatus(dto.getStatus());

        return mapToDTO(payrollRepository.save(existing));
    }

    public void deletePayroll(Long id) {
        if (!payrollRepository.existsById(id)) {
            throw new RuntimeException("Payroll not found with ID: " + id);
        }
        payrollRepository.deleteById(id);
    }

    // Helper methods
    private PayrollDTO mapToDTO(Payroll payroll) {
        return new PayrollDTO(
                payroll.getId(),
                payroll.getEmployeeName(),
                payroll.getCtc(),
                payroll.getSalaryPerMonth(),
                payroll.getDeduction(),
                payroll.getStatus(),
                payroll.getCreatedAt(),
                payroll.getUpdatedAt()
        );
    }

    private Payroll mapToEntity(PayrollDTO dto) {
        Payroll payroll = new Payroll();
        payroll.setId(dto.getId());
        payroll.setEmployeeName(dto.getEmployeeName());
        payroll.setCtc(dto.getCtc());
        payroll.setSalaryPerMonth(dto.getSalaryPerMonth());
        payroll.setDeduction(dto.getDeduction());
        payroll.setStatus(dto.getStatus());
        return payroll;
    }
}
