package org.zerock.b02.service;

import org.zerock.b02.dto.InboundDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;

public interface InboundService {
    // 등록
    Long register(InboundDTO inboundDTO);
    // 1행 조회
    InboundDTO readOne(Long inbound_id);
    // 1행 수정
    void modify(InboundDTO inboundDTO);
    // 1행 삭제
    void remove(Long inbound_id);

    PageResponseDTO list(PageRequestDTO pageRequestDTO);

}
