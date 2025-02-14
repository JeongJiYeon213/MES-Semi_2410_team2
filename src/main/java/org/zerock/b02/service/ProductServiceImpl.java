package org.zerock.b02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b02.domain.Product;
import org.zerock.b02.domain.Stock;
import org.zerock.b02.dto.ProductDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.repository.ProductRepository;
import org.zerock.b02.repository.StockRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
	private final ModelMapper modelMapper;
	private final ProductRepository productRepository;
	private final StockRepository stockRepository;

	@Override
	@Transactional
	public Long register(ProductDTO productDTO) {
		// 제품 등록
		Product product = modelMapper.map(productDTO, Product.class);
		productRepository.save(product);

		// 재고 등록
		boolean stockExists = stockRepository.existsByProduct(product);

		// 재고가 없으면 currentStock = 0 으로 등록
		if (!stockExists) {
			Stock stock = Stock.builder()
					.product(product)
					.currentStock(0L) // 기본 수량 0
					.productCode(product.getProductCode()) // Stock 테이블에 productCode 저장
					.build();
			stockRepository.save(stock);
		}

		return product.getProductId();
	}


	@Override
	public ProductDTO readOne(Long productId) {
		Optional<Product> result = productRepository.findById(productId);
		Product product = result.orElseThrow();
		ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
		return productDTO;

	}

	@Override
	@Transactional
	public void modify(ProductDTO productDTO) {
		Optional<Product> result = productRepository.findById(productDTO.getProductId());
		Product product = result.orElseThrow();
		product.change(productDTO.getProductCode(),
				productDTO.getProductName(),
				productDTO.getProductType(),
				productDTO.getProductWeight(),
				productDTO.getProductSize());
		productRepository.save(product);
	}

	@Override
	public void remove(Long productId) {
		productRepository.deleteById(productId);
	}

	@Override
	public PageResponseDTO list(PageRequestDTO pageRequestDTO) {
		String[] types = pageRequestDTO.getTypes();
		String keyword = pageRequestDTO.getKeyword();
		Pageable pageable = pageRequestDTO.getPageable("productId");

		Page<Product> result = productRepository.searchAll(types, keyword, pageable);

		List<ProductDTO> dtoList = result.getContent().stream()
				.map(product -> modelMapper.map(product, ProductDTO.class))
				.collect(Collectors.toList());

		return PageResponseDTO.<ProductDTO>withAll()
				.pageRequestDTO(pageRequestDTO)
				.dtoList(dtoList)
				.total((int) result.getTotalElements())
				.build();
	}

	@Override
	public List<ProductDTO> getAllProducts() {
		List<Product> products = productRepository.findAll();  // 전체 데이터 가져오기

		log.info("data" + products.size());

		return products.stream()
				.map(product -> modelMapper.map(product, ProductDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public Stock getStockByProductCode(String productCode) {
		// productCode로 재고 조회
		return stockRepository.findByProductCode(productCode)
				.orElseThrow(() -> new IllegalArgumentException("해당 제품 코드에 대한 재고를 찾을 수 없습니다. ProductCode: " + productCode));
	}

}
