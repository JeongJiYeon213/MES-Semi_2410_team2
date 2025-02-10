package com.example.b02.service;

import com.example.b02.dto.PageRequestDTO;
import com.example.b02.dto.PageResponseDTO;
import com.example.b02.dto.SupplierDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class SupplierServiceTests {

    @Autowired
    private SupplierService supplierService;

    @Test
    public void testRegister(){

        log.info(supplierService.getClass().getName());

        SupplierDTO supplierDTO = SupplierDTO.builder()
                .supplierId("jang")
                .supplierName("test1")
                .supplierInfo("test1")
                .build();

        Long bno = supplierService.register(supplierDTO);

        log.info("bno: " + bno);
    }

    @Test
    public void testModify(){

        SupplierDTO supplierDTO = SupplierDTO.builder()
                .bno(2L)
                .supplierId("updateId3")
                .supplierName("updateName1")
                .supplierInfo("00000000")
                .build();

        supplierService.modify(supplierDTO);
    }

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("inf")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO responseDTO = supplierService.list(pageRequestDTO);

        log.info(responseDTO);

    }
}
