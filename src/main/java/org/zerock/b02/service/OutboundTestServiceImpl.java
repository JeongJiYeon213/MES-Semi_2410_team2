package org.zerock.b02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b02.domain.OutboundTest;
import org.zerock.b02.domain.Stock;
import org.zerock.b02.dto.OutboundTestDTO;
import org.zerock.b02.repository.InboundTestRepository;
import org.zerock.b02.repository.OutboundTestRepository;
import org.zerock.b02.repository.StockRepository;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class OutboundTestServiceImpl implements OutboundTestService {
	private final OutboundTestRepository outboundTestRepository;
	private final InboundTestRepository inboundRepository;
	private final StockRepository stockRepository;
	private final ModelMapper modelMapper;

	@Override
	public Long register(OutboundTestDTO outboundTestDTO) {
		Stock stock = stockRepository.findById(outboundTestDTO.getStockId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid stock ID"));

		if (stock.getCurrentStock() < outboundTestDTO.getMinus()) {
			throw new IllegalStateException("출고 수량이 재고보다 많습니다.");
		}

		OutboundTest outbound = OutboundTest.builder()
				.stock(stock)
				.minus(outboundTestDTO.getMinus())
				.build();

		stock.setCurrentStock(stock.getCurrentStock() - outboundTestDTO.getMinus());

		outboundTestRepository.save(outbound);
		stockRepository.save(stock);

		return outbound.getOutboundId();
	}

}
