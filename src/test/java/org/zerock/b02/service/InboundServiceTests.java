package org.zerock.b02.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b02.domain.Inbound;
import org.zerock.b02.dto.InboundDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;

import java.time.LocalDateTime;

@SpringBootTest
@Log4j2
public class InboundServiceTests {
    @Autowired
    private InboundService inboundService;

    @Test
    public void inboundRegisterTest(){
        log.info(inboundService.getClass().getName());

        InboundDTO inboundDTO = InboundDTO.builder()
                .quantity(1L)
                .description("inbound 테스팅")
                .inboundStatus("CANCELED")
                .build();
    }

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("a")
                .keyword("i")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO responseDTO = inboundService.list(pageRequestDTO);

        log.info(responseDTO);
    }

    @Test
    public void testModify(){
        InboundDTO inboundDTO = InboundDTO.builder()
                .inboundId(1L)
                .quantity(112L)
                .inboundStatus("CANCELED")
                .inboundDate(LocalDateTime.now())
                .description("modify testing")
                .build();
        inboundService.modify(inboundDTO);
    }
}
