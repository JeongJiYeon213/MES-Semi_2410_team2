package org.zerock.b02.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b02.domain.Inbound;
import org.zerock.b02.dto.InboundDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class InboundRepositoryTests {
    @Autowired
    private InboundRepository inboundRepository;

    @Test
    public void testJpaInsert() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Inbound inbound = Inbound.builder()
                    .inboundCode("i123")
                    .description("inbound testing")
                    .quantity(1L)
                    .inboundDate(LocalDateTime.now())
                    .inboundStatus("APPROVED")
                    .build();

            // jpa insert 기능으로 board 테이블에 데이터 저장
            Inbound result = inboundRepository.save(inbound);
            log.info("inbound_id: " + result.getInboundId());
        });
    }

    @Test
    public void testJpaSelectAll(){
        List<Inbound> result = inboundRepository.findAll();
        result.forEach(item -> log.info(item));
    }

    @Test
    public void testselectOne(){
        Optional<Inbound> result = inboundRepository.findById(1L);
        Inbound inbound = result.orElseThrow();

        log.info(inbound);
    }

}
