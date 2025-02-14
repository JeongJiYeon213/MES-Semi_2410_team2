package org.zerock.b02.service;

import org.zerock.b02.dto.InboundTestDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;

public interface InboundTestService {
	Long register(InboundTestDTO inboundTestDTO);

}
