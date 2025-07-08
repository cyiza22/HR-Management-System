package api.example.hrm_system.payroll;

import api.example.hrm_system.employee.Employee;
import api.example.hrm_system.employee.EmployeeRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;

    public PayrollService(PayrollRepository payrollRepository, EmployeeRepository employeeRepository) {
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
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

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + dto.getEmployeeId()));

        existing.setEmployee(employee);
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

    public byte[] exportPayrollToPdf() {
        List<PayrollDTO> payrolls = getAllPayrolls();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("Payroll Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            float[] columnWidths = {2f, 1.5f, 1.5f, 1f, 1f, 1.5f};
            table.setWidths(columnWidths);

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            PdfPCell cell;

            String[] headers = {"Employee", "CTC", "Monthly Salary", "Deduction", "Status", "Created At"};
            for (String header : headers) {
                cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(BaseColor.DARK_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);
            }

            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            for (PayrollDTO payroll : payrolls) {
                table.addCell(new Phrase(payroll.getEmployeeName(), dataFont));
                table.addCell(new Phrase(payroll.getCtc().toString(), dataFont));
                table.addCell(new Phrase(payroll.getSalaryPerMonth().toString(), dataFont));
                table.addCell(new Phrase(payroll.getDeduction() != null ? payroll.getDeduction().toString() : "0", dataFont));
                table.addCell(new Phrase(payroll.getStatus().name(), dataFont));
                table.addCell(new Phrase(payroll.getCreatedAt().toString(), dataFont));
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
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