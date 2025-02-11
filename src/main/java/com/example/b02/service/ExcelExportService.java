package com.example.b02.service;

import com.example.b02.dto.CustomerDTO;
import com.example.b02.dto.SupplierDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

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

        String[] headers = type.equals("customer") ?
                new String[]{"NO","ID","고객명","전화번호","둥록날짜","수정날짜"} :
                new String[]{"NO"};

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

            if (obj instanceof CustomerDTO) {
                CustomerDTO customer = (CustomerDTO) obj;
                row.createCell(0).setCellValue(customer.getBno());
                row.createCell(1).setCellValue(customer.getCustomerId());
                row.createCell(2).setCellValue(customer.getCustomerName());
                row.createCell(3).setCellValue(customer.getCustomerInfo());
                row.createCell(4).setCellValue(customer.getRegDate().format(formatter));
            } else if (obj instanceof SupplierDTO) {
                SupplierDTO supplier = (SupplierDTO) obj;
                row.createCell(0).setCellValue(supplier.getBno());
                row.createCell(1).setCellValue(supplier.getSupplierId());
                row.createCell(2).setCellValue(supplier.getSupplierName());
                row.createCell(3).setCellValue(supplier.getSupplierInfo());
                row.createCell(4).setCellValue(supplier.getRegDate().format(formatter));
            }

            for (int i = 0; i < sheet.getRow(rowIdx).getLastCellNum(); i++) {
                sheet.getRow(rowIdx).getCell(i).setCellStyle(rowStyle);
            }
            rowIdx++;
        }

        for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
            sheet.autoSizeColumn(i);
            int currentWidth = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, currentWidth + 256 * 2);
        }
    }
}