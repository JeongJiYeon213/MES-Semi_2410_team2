package org.zerock.b02.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b02.dto.AdminDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;

@SpringBootTest
@Log4j2
public class AdminServiceTests {
    @Autowired
    private AdminService adminService;

    @Test
    public void testAdmin() {
        log.info(adminService.getClass().getName());

        AdminDTO adminDTO = AdminDTO.builder()
                .adminId(12345678L)
                .adminName("asd")
                .adminPassword(1111)
                .phoneNum("01012345678")
                .position("사원")
                .build();

        Long bno = adminService.register(adminDTO);
        log.info("bno: " + bno);
    }

    @Test
    public void testModify() {

        AdminDTO adminDTO = AdminDTO.builder()
                .bno(111L)
                .adminName("qweasd")
                .adminId(1234651L)
                .build();

        adminService.modify(adminDTO);
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("t")
                .keyword("For")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO responseDTO = adminService.list(pageRequestDTO);

        log.info(responseDTO);
    }
}
