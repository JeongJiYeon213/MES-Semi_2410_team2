package com.example.b02.service;

import com.example.b02.domain.Admin;
import com.example.b02.dto.AdminDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class AdminServiceTests {

    @Autowired
    private AdminService adminService;

    @Test
    public void testRegister(){

        log.info(adminService.getClass().getName());

            AdminDTO adminDTO = AdminDTO.builder()
                    .adminId("maria3")
                    .password("1234")
                    .adminName("마리아")
                    .email("hahaha@naver.com")
                    .department("과장")
                    .position("회계")
                    .build();

            Long bno = adminService.register(adminDTO);

            log.info("bno: " + bno);

    }
}
