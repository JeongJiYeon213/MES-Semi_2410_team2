package org.zerock.b02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.zerock.b02.dto.AdminDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ExcelExportService {

    public <E> byte[] generateExcel(String type, List<E> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(type);
            createHeader(sheet, workbook, type);
            populateData(sheet, workbook, data, type);
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void createHeader(Sheet sheet, Workbook workbook, String type) {
        Row headerRow = sheet.createRow(0);
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);

        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 헤더를 "admin" 타입에 맞게 설정
        String[] headers = type.equals("admin") ?
                new String[]{"수정날짜", "등록날짜", "사원번호", "사원이름", "비밀번호", "전화번호", "직급"} :
                new String[]{};  // 다른 경우는 빈 배열로 처리

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerCellStyle);
        }
    }

    private <E> void populateData(Sheet sheet, Workbook workbook, List<E> data, String type) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        CellStyle evenRowStyle = workbook.createCellStyle();
        evenRowStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        evenRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle oddRowStyle = workbook.createCellStyle();
        oddRowStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        oddRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        int rowIdx = 1;
        for (E obj : data) {
            Row row = sheet.createRow(rowIdx);
            CellStyle rowStyle = (rowIdx % 2 == 0) ? evenRowStyle : oddRowStyle;

            // AdminDTO만 처리하도록 변경
            if (obj instanceof AdminDTO) {
                AdminDTO admin = (AdminDTO) obj;
                row.createCell(0).setCellValue(admin.getModDate().format(formatter));
                row.createCell(1).setCellValue(admin.getRegDate().format(formatter));
                row.createCell(2).setCellValue(admin.getAdminId());
                row.createCell(3).setCellValue(admin.getAdminName());
                row.createCell(4).setCellValue(admin.getAdminPassword());
                row.createCell(5).setCellValue(admin.getPhoneNum());
                row.createCell(6).setCellValue(admin.getPosition());
            } else {
                throw new IllegalArgumentException("Unsupported type: " + obj.getClass().getName());
            }

            // 스타일 적용
            for (int i = 0; i < sheet.getRow(rowIdx).getLastCellNum(); i++) {
                sheet.getRow(rowIdx).getCell(i).setCellStyle(rowStyle);
            }
            rowIdx++;
        }

        // 자동 크기 조정
        for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
            sheet.autoSizeColumn(i);
            int currentWidth = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, currentWidth + 256 * 2);
        }
    }
}
