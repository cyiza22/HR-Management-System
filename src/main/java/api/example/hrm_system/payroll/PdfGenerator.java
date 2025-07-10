package api.example.hrm_system.payroll;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Component
public class PdfGenerator {

    public byte[] generatePayrollPdf(List<PayrollDTO> payrollList) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Payroll Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Create table
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Set column widths
            float[] columnWidths = {2f, 1.5f, 1.5f, 1f, 1f, 1.5f};
            table.setWidths(columnWidths);

            // Add table headers
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            String[] headers = {"Employee", "CTC", "Monthly Salary", "Deduction", "Status", "Created At"};

            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(BaseColor.DARK_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);
            }

            // Add table data
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            for (PayrollDTO payroll : payrollList) {
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
            throw new RuntimeException("Error generating PDF", e);
        }

        return out.toByteArray();
    }
}