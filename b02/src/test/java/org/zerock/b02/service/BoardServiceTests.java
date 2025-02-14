package org.zerock.b02.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b02.dto.BoardDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;

import java.time.LocalDateTime;

@SpringBootTest
@Log4j2
public class BoardServiceTests {
    @Autowired
    private BoardService boardService;

    @Test
    public void testAdmin() {
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .adminId(12345678L)
                .adminName("asd")
                .adminPassword(1111)
                .phoneNum("01012345678")
                .position("사원")
                .build();

        Long bno = boardService.register(boardDTO);
        log.info("bno: " + bno);
    }

    @Test
    public void testModify() {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(111L)
                .adminName("qweasd")
                .adminId(1234651L)
                .build();

        boardService.modify(boardDTO);
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("t")
                .keyword("For")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);
    }
}
