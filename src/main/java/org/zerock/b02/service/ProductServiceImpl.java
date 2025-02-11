package org.zerock.b02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b02.domain.Product;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.dto.ProductDTO;
import org.zerock.b02.repository.ProductRepository;

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

    @Override
    public Long register(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        Long productId = productRepository.save(product).getProductId();
        return productId;

    }

    @Override
    public ProductDTO readOne(Long productId) {
        Optional<Product> result = productRepository.findById(productId);
        Product product = result.orElseThrow();
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        return productDTO;

    }

    @Override
    public void modify(ProductDTO productDTO) {
        Optional<Product> result = productRepository.findById(productDTO.getProductId());
        Product product = result.orElseThrow();
        product.change(productDTO.getProductCode(), productDTO.getProductName());
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

        return PageResponseDTO.<ProductDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    public List<ProductDTO> getAllProducts(){
        return productRepository.findAll().stream().map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
    }

}