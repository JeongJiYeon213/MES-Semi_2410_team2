package org.zerock.b02.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b02.domain.Inbound;

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
                    .description("inbound testing")
                    .productId(1L)
                    .quantity(1L)
                    .inboundDate(LocalDateTime.now())
                    .inboundStatus(Inbound.Status.COMPLETED)
                    .supplierId(1L)
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
    public void testupdate(){
        Optional<Inbound> result = inboundRepository.findById(10L);
        Inbound inbound = result.orElseThrow();

        inbound.change(2L, 2L,2L, LocalDateTime.now(), "updatetest", Inbound.Status.PENDING);
        inboundRepository.save(inbound);
    }
}
