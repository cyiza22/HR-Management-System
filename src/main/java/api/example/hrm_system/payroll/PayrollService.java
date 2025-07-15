package api.example.hrm_system.payroll;

import api.example.hrm_system.employee.Employee;
import api.example.hrm_system.employee.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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



    @Transactional
    public PayrollDTO createPayroll(PayrollDTO dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + dto.getEmployeeId()));

        Payroll payroll = mapToEntity(dto);
        payroll.setEmployee(employee);

        Payroll saved = payrollRepository.save(payroll);
        log.info("Created payroll for employee: {}", employee.getEmail());

        return mapToDTO(saved);
    }


    public List<PayrollDTO> getAllPayrolls() {
        return payrollRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PayrollDTO getPayrollById(Long id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found with ID: " + id));
        return mapToDTO(payroll);
    }

    public List<PayrollDTO> getPayrollByEmployeeEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));

        return payrollRepository.findByEmployeeId(employee.getId()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PayrollDTO> getPayrollsByEmployeeId(Long employeeId) {
        return payrollRepository.findByEmployeeId(employeeId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PayrollDTO> getPayrollByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId).stream()
                .flatMap(employee -> payrollRepository.findByEmployeeId(employee.getId()).stream())
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PayrollDTO> getPayrollsByStatus(Payroll.PayrollStatus status) {
        return payrollRepository.findByStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public PayrollDTO updatePayroll(Long id, PayrollDTO dto) {
        Payroll existing = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found with ID: " + id));

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + dto.getEmployeeId()));


        existing.setCtc(dto.getCtc());
        existing.setSalaryPerMonth(dto.getSalaryPerMonth());
        existing.setDeduction(dto.getDeduction());
        existing.setBankName(dto.getBankName());
        existing.setBankAccount(dto.getBankAccount());
        existing.setStatus(dto.getStatus());
        existing.setEmployee(employee);

        Payroll saved = payrollRepository.save(existing);
        log.info("Updated payroll for employee: {}", employee.getEmail());

        return mapToDTO(saved);
    }

    @Transactional
    public PayrollDTO updatePayrollStatus(Long id, Payroll.PayrollStatus status) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found with ID: " + id));

        payroll.setStatus(status);
        Payroll saved = payrollRepository.save(payroll);

        return mapToDTO(saved);
    }



    @Transactional
    public void deletePayroll(Long id) {
        if (!payrollRepository.existsById(id)) {
            throw new RuntimeException("Payroll not found with ID: " + id);
        }
        payrollRepository.deleteById(id);
        log.info("Deleted payroll with ID: {}", id);
    }


    public byte[] generatePayrollPdf(List<PayrollDTO> payrolls) {
        return pdfGenerator.generatePayrollPdf(payrolls);
    }



    private Payroll mapToEntity(PayrollDTO dto) {
        Payroll payroll = new Payroll();
        payroll.setId(dto.getId());
        payroll.setCtc(dto.getCtc());
        payroll.setSalaryPerMonth(dto.getSalaryPerMonth());
        payroll.setDeduction(dto.getDeduction());
        payroll.setBankName(dto.getBankName());
        payroll.setBankAccount(dto.getBankAccount());
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
        dto.setBankName(payroll.getBankName());
        dto.setBankAccount(payroll.getBankAccount());
        dto.setStatus(payroll.getStatus());
        dto.setCreatedAt(payroll.getCreatedAt());
        dto.setUpdatedAt(payroll.getUpdatedAt());
        return dto;
    }
}