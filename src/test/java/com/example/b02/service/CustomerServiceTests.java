package com.example.b02.service;

import com.example.b02.dto.CustomerDTO;
import com.example.b02.dto.PageRequestDTO;
import com.example.b02.dto.PageResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
@Log4j2
public class CustomerServiceTests {

    @Autowired
    private CustomerService customerService;

    @Test
    public void testRegister(){

        log.info(customerService.getClass().getName());

        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerId("a3")
                .customerName("test")
                .customerInfo("test")
                .build();

        Long bno = customerService.register(customerDTO);

        log.info("bno: " + bno);
    }

    @Test
    public void testModify(){

        CustomerDTO customerDTO = CustomerDTO.builder()
                .bno(1L)
                .customerId("updateId1")
                .customerName("updateName")
                .customerInfo("000000000")
                .build();

        customerService.modify(customerDTO);
    }

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("inf")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO responseDTO = customerService.list(pageRequestDTO);

        log.info(responseDTO);

    }
}
