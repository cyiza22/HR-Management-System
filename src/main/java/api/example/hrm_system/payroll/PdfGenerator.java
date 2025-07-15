package api.example.hrm_system.payroll;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Component
public class PdfGenerator {

    public byte[] generatePayrollPdf(List<PayrollDTO> payrollList) {
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add title with company header
            addHeader(document);

            // Add payroll table
            addPayrollTable(document, payrollList);

            // Add summary
            addSummary(document, payrollList);

            // Add footer
            addFooter(document);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        return out.toByteArray();
    }

    private void addHeader(Document document) throws DocumentException {
        // Company header
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.DARK_GRAY);
        Paragraph title = new Paragraph("HRM SYSTEM", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(5);
        document.add(title);

        // Report title
        Font reportFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Paragraph reportTitle = new Paragraph("PAYROLL REPORT", reportFont);
        reportTitle.setAlignment(Element.ALIGN_CENTER);
        reportTitle.setSpacingAfter(10);
        document.add(reportTitle);

        // Generated date
        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
        Paragraph dateGenerated = new Paragraph("Generated on: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")), dateFont);
        dateGenerated.setAlignment(Element.ALIGN_RIGHT);
        dateGenerated.setSpacingAfter(20);
        document.add(dateGenerated);

        // Add separator line
        LineSeparator separator = new LineSeparator();
        separator.setLineColor(BaseColor.LIGHT_GRAY);
        document.add(new Chunk(separator));
        document.add(Chunk.NEWLINE);
    }

    private void addPayrollTable(Document document, List<PayrollDTO> payrollList) throws DocumentException {
        // Create table with 7 columns
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Set column widths
        float[] columnWidths = {3f, 2f, 2f, 1.5f, 1.5f, 2.5f, 2f};
        table.setWidths(columnWidths);

        // Add table headers
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
        String[] headers = {"Employee", "CTC", "Monthly Salary", "Deduction", "Net Salary", "Bank Name", "Bank Account"};

        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(new BaseColor(52, 73, 94)); // Dark blue-gray
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }

        // Add table data
        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
        Font numberFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.DARK_GRAY);

        boolean isAlternateRow = false;
        for (PayrollDTO payroll : payrollList) {
            BaseColor rowColor = isAlternateRow ? new BaseColor(248, 249, 250) : BaseColor.WHITE;

            // Employee Name
            addCell(table, getEmployeeName(payroll),
                    dataFont, rowColor, Element.ALIGN_LEFT);

            // CTC
            addCell(table, formatCurrency(getCtc(payroll)),
                    numberFont, rowColor, Element.ALIGN_RIGHT);

            // Monthly Salary
            addCell(table, formatCurrency(getSalaryPerMonth(payroll)),
                    numberFont, rowColor, Element.ALIGN_RIGHT);

            // Deduction
            addCell(table, formatCurrency(getDeduction(payroll)),
                    numberFont, rowColor, Element.ALIGN_RIGHT);

            // Net Salary
            addCell(table, formatCurrency(getNetSalary(payroll)),
                    numberFont, rowColor, Element.ALIGN_RIGHT);

            // Bank Name
            addCell(table, getBankName(payroll),
                    dataFont, rowColor, Element.ALIGN_LEFT);

            // Bank Account
            addCell(table, getBankAccount(payroll),
                    dataFont, rowColor, Element.ALIGN_CENTER);

            isAlternateRow = !isAlternateRow;
        }

        document.add(table);
    }

    private void addCell(PdfPTable table, String content, Font font, BaseColor backgroundColor, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setBackgroundColor(backgroundColor);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    private void addSummary(Document document, List<PayrollDTO> payrollList) throws DocumentException {
        // Calculate totals
        BigDecimal totalCTC = payrollList.stream()
                .map(this::getCtc)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSalary = payrollList.stream()
                .map(this::getSalaryPerMonth)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDeductions = payrollList.stream()
                .map(this::getDeduction)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalNet = payrollList.stream()
                .map(this::getNetSalary)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        document.add(Chunk.NEWLINE);

        Font summaryHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.DARK_GRAY);
        Paragraph summaryHeader = new Paragraph("PAYROLL SUMMARY", summaryHeaderFont);
        summaryHeader.setSpacingBefore(10);
        summaryHeader.setSpacingAfter(10);
        document.add(summaryHeader);

        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(50);
        summaryTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        Font summaryFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
        Font summaryValueFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.DARK_GRAY);

        addSummaryRow(summaryTable, "Total Employees:", String.valueOf(payrollList.size()), summaryFont, summaryValueFont);
        addSummaryRow(summaryTable, "Total CTC:", formatCurrency(totalCTC), summaryFont, summaryValueFont);
        addSummaryRow(summaryTable, "Total Monthly Salary:", formatCurrency(totalSalary), summaryFont, summaryValueFont);
        addSummaryRow(summaryTable, "Total Deductions:", formatCurrency(totalDeductions), summaryFont, summaryValueFont);
        addSummaryRow(summaryTable, "Total Net Payroll:", formatCurrency(totalNet), summaryFont, summaryValueFont);

        document.add(summaryTable);
    }

    private void addSummaryRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(4);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(4);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(valueCell);
    }

    private void addFooter(Document document) throws DocumentException {
        document.add(Chunk.NEWLINE);
        LineSeparator separator = new LineSeparator();
        separator.setLineColor(BaseColor.LIGHT_GRAY);
        document.add(new Chunk(separator));
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.GRAY);
        Paragraph footer = new Paragraph("This is a system-generated report. For any queries, contact HR department.", footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(10);
        document.add(footer);
    }
    private String getEmployeeName(PayrollDTO payroll) {
        return payroll.getEmployeeName() != null ? payroll.getEmployeeName() : "N/A";
    }

    private BigDecimal getCtc(PayrollDTO payroll) {
        return payroll.getCtc();
    }

    private BigDecimal getSalaryPerMonth(PayrollDTO payroll) {
        return payroll.getSalaryPerMonth();
    }

    private BigDecimal getDeduction(PayrollDTO payroll) {
        return payroll.getDeduction();
    }

    private BigDecimal getNetSalary(PayrollDTO payroll) {
        return payroll.getNetSalary();
    }

    private String getBankName(PayrollDTO payroll) {
        return payroll.getBankName() != null ? payroll.getBankName() : "Not Provided";
    }

    private String getBankAccount(PayrollDTO payroll) {
        String account = payroll.getBankAccount();
        if (account != null) {
            return maskBankAccount(account);
        }
        return "Not Provided";
    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "$0.00";
        }
        return String.format("$%,.2f", amount);
    }

    private String maskBankAccount(String bankAccount) {
        if (bankAccount == null || bankAccount.length() < 4) {
            return bankAccount;
        }
        String lastFour = bankAccount.substring(bankAccount.length() - 4);
        return "****" + lastFour;
    }
}