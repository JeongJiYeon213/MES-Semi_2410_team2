package org.zerock.b02.service;

import org.zerock.b02.dto.InboundTestDTO;
import org.zerock.b02.dto.OutboundTestDTO;

public interface OutboundTestService {
	Long register(OutboundTestDTO outboundTestDTO);
}
