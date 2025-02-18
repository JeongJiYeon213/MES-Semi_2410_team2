package org.zerock.b02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.b02.domain.Product;
import org.zerock.b02.domain.Stock;
import org.zerock.b02.dto.StockDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.repository.ProductRepository;
import org.zerock.b02.repository.StockRepository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class StockServiceImpl implements StockService {
	private final ModelMapper modelMapper;
	private final StockRepository stockRepository;
	private final ProductRepository productRepository;

	@Override
	@Transactional
	public Long register(StockDTO stockDTO) {
		Product product = productRepository.findById(stockDTO.getProductId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

		boolean exists = stockRepository.existsByProduct(product);
		if (exists) {
			throw new IllegalArgumentException("이미 등록된 제품입니다.");
		}

		Stock stock = Stock.builder()
				.product(product)  // 기존 영속 상태의 Product 객체 사용
				.currentStock(stockDTO.getCurrentStock())
				.build();

		stockRepository.save(stock);
		return stock.getStockId();
	}

	@Override
	public StockDTO readOne(Long stockId) {
		Optional<Stock> result = stockRepository.findById(stockId);
		Stock stock = result.orElseThrow();

		StockDTO stockDTO = modelMapper.map(stock, StockDTO.class);
		stockDTO.setProductCode(stock.getProduct().getProductCode());
		stockDTO.setRegDate(stock.getRegDate());
		stockDTO.setModDate(stock.getModDate());
		return stockDTO;
	}

	@Override
	public void modify(StockDTO stockDTO) {
		Optional<Stock> result = stockRepository.findById(stockDTO.getStockId());
		Stock stock = result.orElseThrow();
		stock.change(stockDTO.getCurrentStock());
		stockRepository.save(stock);
	}

	@Override
	public void remove(Long stockId) {
		stockRepository.deleteById(stockId);
	}

	@Override
	public PageResponseDTO list(PageRequestDTO pageRequestDTO) {
		String[] types = pageRequestDTO.getTypes();
		String keyword = pageRequestDTO.getKeyword();
		Pageable pageable = pageRequestDTO.getPageable("productId");

		Page<Stock> result = stockRepository.searchAll(types, keyword, pageable);

		List<StockDTO> dtoList = result.getContent().stream()
				.map(stock -> {
					StockDTO stockDTO = modelMapper.map(stock, StockDTO.class);
					stockDTO.setProductCode(stock.getProduct().getProductCode());
					return stockDTO;
				})
				.collect(Collectors.toList());

		return PageResponseDTO.<StockDTO>withAll()
				.pageRequestDTO(pageRequestDTO)
				.dtoList(dtoList)
				.total((int) result.getTotalElements())
				.build();
	}

	@Override
	public List<StockDTO> getAllStocks() {
		List<Stock> stocks = stockRepository.findAll();  // 전체 데이터 가져오기

		log.info("data" + stocks.size());

		return stocks.stream()
				.map(stock -> {
					StockDTO stockDTO = modelMapper.map(stock, StockDTO.class);
					stockDTO.setProductCode(stock.getProduct().getProductCode());
					return stockDTO;
				})
				.collect(Collectors.toList());
	}
}