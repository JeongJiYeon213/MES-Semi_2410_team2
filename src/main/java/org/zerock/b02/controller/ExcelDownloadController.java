package org.zerock.b02.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.b02.dto.AdminDTO;
import org.zerock.b02.service.AdminService;
import org.zerock.b02.service.ExcelExportService;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/excel")
public class ExcelDownloadController {

    private final AdminService adminService;


    private final ExcelExportService excelExportService;

    @GetMapping("/admin/download")
    public ResponseEntity<byte[]> downloadExcelAdmin() throws IOException {
        List<AdminDTO> adminDTOList = adminService.getAllAdmins();
        byte[] excelData = excelExportService.generateExcel("admin", adminDTOList);

        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        String fileName = "admin_" + currentDate + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }




}