package api.example.hrm_system.payroll;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class PdfGenerator {

    public static ByteArrayInputStream generatePayrollPdf(List<PayrollDTO> payrollList) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Payroll Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{3, 2, 2, 2, 2, 3});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            String[] headers = {"Employee", "CTC", "Monthly Salary", "Deduction", "Status", "Created At"};

            for (String header : headers) {
                PdfPCell hCell = new PdfPCell(new Phrase(header, headFont));
                hCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(hCell);
            }

            for (PayrollDTO dto : payrollList) {
                table.addCell(dto.getEmployeeName());
                table.addCell(dto.getCtc().toString());
                table.addCell(dto.getSalaryPerMonth().toString());
                table.addCell(dto.getDeduction() != null ? dto.getDeduction().toString() : "0");
                table.addCell(dto.getStatus().name());
                table.addCell(dto.getCreatedAt().toString());
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}

