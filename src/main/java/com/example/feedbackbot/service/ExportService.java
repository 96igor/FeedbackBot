package com.example.feedbackbot.service;

import com.example.feedbackbot.model.Feedback;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExportService {

    public void exportToExcel(List<Feedback> feedbackList, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Feedbacks");

        // Заголовки
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Role");
        header.createCell(1).setCellValue("Branch");
        header.createCell(2).setCellValue("Message");
        header.createCell(3).setCellValue("Sentiment");
        header.createCell(4).setCellValue("Severity");

        // Данные
        int rowNum = 1;
        for (Feedback fb : feedbackList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(fb.getRole());
            row.createCell(1).setCellValue(fb.getBranch());
            row.createCell(2).setCellValue(fb.getMessage());  // <- вместо getText()
            row.createCell(3).setCellValue(fb.getSentiment());
            row.createCell(4).setCellValue(fb.getSeverity()); // <- вместо getCriticality()
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
        workbook.close();
    }
}
