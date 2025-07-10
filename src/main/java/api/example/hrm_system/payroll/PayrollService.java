package api.example.hrm_system.payroll;

import api.example.hrm_system.employee.Employee;
import api.example.hrm_system.employee.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final PdfGenerator pdfGenerator;

    public PayrollService(PayrollRepository payrollRepository,
                          EmployeeRepository employeeRepository,
                          PdfGenerator pdfGenerator) {
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
        this.pdfGenerator = pdfGenerator;
    }

    public PayrollDTO createPayroll(PayrollDTO dto) {
        Payroll payroll = mapToEntity(dto);
        return mapToDTO(payrollRepository.save(payroll));
    }

    public List<PayrollDTO> getAllPayrolls() {
        return payrollRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PayrollDTO getPayrollById(Long id) {
        return payrollRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Payroll not found with ID: " + id));
    }

    public List<PayrollDTO> getPayrollByEmployeeEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));

        return payrollRepository.findByEmployeeId(employee.getId()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PayrollDTO> getPayrollByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId).stream()
                .flatMap(employee -> payrollRepository.findByEmployeeId(employee.getId()).stream())
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public byte[] exportPayrollToPdf() {
        List<PayrollDTO> payrolls = getAllPayrolls();
        return pdfGenerator.generatePayrollPdf(payrolls);
    }

    public PayrollDTO updatePayroll(Long id, PayrollDTO dto) {
        return payrollRepository.findById(id)
                .map(existing -> {
                    Employee employee = employeeRepository.findById(dto.getEmployeeId())
                            .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + dto.getEmployeeId()));

                    existing.setEmployee(employee);
                    existing.setCtc(dto.getCtc());
                    existing.setSalaryPerMonth(dto.getSalaryPerMonth());
                    existing.setDeduction(dto.getDeduction());
                    existing.setStatus(dto.getStatus());

                    return mapToDTO(payrollRepository.save(existing));
                })
                .orElseThrow(() -> new RuntimeException("Payroll not found with ID: " + id));
    }

    public void deletePayroll(Long id) {
        if (!payrollRepository.existsById(id)) {
            throw new RuntimeException("Payroll not found with ID: " + id);
        }
        payrollRepository.deleteById(id);
    }

    private Payroll mapToEntity(PayrollDTO dto) {
        Payroll payroll = new Payroll();
        payroll.setId(dto.getId());

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + dto.getEmployeeId()));
        payroll.setEmployee(employee);

        payroll.setCtc(dto.getCtc());
        payroll.setSalaryPerMonth(dto.getSalaryPerMonth());
        payroll.setDeduction(dto.getDeduction());
        payroll.setStatus(dto.getStatus());
        return payroll;
    }

    private PayrollDTO mapToDTO(Payroll payroll) {
        PayrollDTO dto = new PayrollDTO();
        dto.setId(payroll.getId());
        dto.setEmployeeId(payroll.getEmployee().getId());
        dto.setEmployeeName(payroll.getEmployee().getFirstName() + " " + payroll.getEmployee().getLastName());
        dto.setCtc(payroll.getCtc());
        dto.setSalaryPerMonth(payroll.getSalaryPerMonth());
        dto.setDeduction(payroll.getDeduction());
        dto.setStatus(payroll.getStatus());
        dto.setCreatedAt(payroll.getCreatedAt());
        dto.setUpdatedAt(payroll.getUpdatedAt());
        return dto;
    }
}