package org.zerock.b02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b02.domain.InboundTest;
import org.zerock.b02.domain.Stock;
import org.zerock.b02.dto.InboundTestDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.repository.InboundTestRepository;
import org.zerock.b02.repository.StockRepository;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class InboundTestServiceImpl implements InboundTestService {
	private final InboundTestRepository inboundRepository;
	private final StockRepository stockRepository;
	private final ModelMapper modelMapper;

	@Override
	public Long register(InboundTestDTO inboundTestDTO) {
		Stock stock = stockRepository.findById(inboundTestDTO.getStockId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid stock ID"));

		// 입고 처리
		InboundTest inbound = InboundTest.builder()
				.stock(stock)
				.plus(inboundTestDTO.getPlus())
				.build();

		// Stock의 currentStock 증가
		stock.setCurrentStock(stock.getCurrentStock() + inboundTestDTO.getPlus());

		inboundRepository.save(inbound);
		stockRepository.save(stock);

		return inbound.getInboundId();
	}


}
