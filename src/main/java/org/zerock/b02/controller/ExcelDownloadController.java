package org.zerock.b02.controller;

import org.zerock.b02.dto.CustomerDTO;
import org.zerock.b02.dto.SupplierDTO;
import org.zerock.b02.service.CustomerService;
import org.zerock.b02.service.ExcelExportService;
import org.zerock.b02.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/excel")
public class ExcelDownloadController {

    private final CustomerService customerService;
    private final SupplierService supplierService;
    private final ExcelExportService excelExportService;

    @GetMapping("/customer/download")
    public ResponseEntity<byte[]> downloadExcelCustomer() throws IOException {
        List<CustomerDTO> customerDTOList = customerService.getAllCustomers();
        byte[] excelData = excelExportService.generateExcel("customer", customerDTOList);

        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        String fileName = "customer_" + currentDate + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }

    @GetMapping("/supplier/download")
    public ResponseEntity<byte[]> downloadExcelSupplier() throws IOException {
        List<SupplierDTO> supplierDTOList = supplierService.getAllSuppliers();
        byte[] excelData = excelExportService.generateExcel("supplier", supplierDTOList);

        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        String fileName = "supplier_" + currentDate + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }
}