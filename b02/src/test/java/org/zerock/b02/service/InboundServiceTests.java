package org.zerock.b02.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b02.domain.Inbound;
import org.zerock.b02.dto.InboundDTO;

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
                .inboundStatus(InboundDTO.Status.CANCELED)
                .build();
    }
}
