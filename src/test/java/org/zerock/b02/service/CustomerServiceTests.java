package org.zerock.b02.service;

import org.zerock.b02.domain.Customer;
import org.zerock.b02.dto.CustomerDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Log4j2
public class CustomerServiceTests {

    @Autowired
    private CustomerService customerService;

    @Test
    public void testRegister(){

        log.info(customerService.getClass().getName());

        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerId("test1")
                .customerName("test")
                .customerInfo("test")
                .build();

        String customerId = customerService.register(customerDTO);

        log.info("customerId: " + customerId);
    }

    @Test
    public void testModify(){

        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerName("updateName")
                .customerInfo("000000000")
                .build();

        customerService.modify(customerDTO);
    }

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("inf")
                .keyword("Sample")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO responseDTO = customerService.list(pageRequestDTO);

        log.info(responseDTO);

    }



}
