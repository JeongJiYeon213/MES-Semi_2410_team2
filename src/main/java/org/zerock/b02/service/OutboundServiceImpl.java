package org.zerock.b02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.b02.domain.Inbound;
import org.zerock.b02.domain.Outbound;
import org.zerock.b02.domain.Product;
import org.zerock.b02.domain.Supplier;
import org.zerock.b02.dto.InboundDTO;
import org.zerock.b02.dto.OutboundDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.repository.OutboundRepository;
import org.zerock.b02.repository.ProductRepository;
import org.zerock.b02.repository.SupplierRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class OutboundServiceImpl implements OutboundService{
    private final ModelMapper modelMapper;
    private final OutboundRepository outboundRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    @Override
    public Long register(OutboundDTO outboundDTO){
        Optional<Outbound> lastOutbound = outboundRepository.findTopByOrderByOutboundCodeDesc();

        String newOutboundCode = generateNewOutboundCode(lastOutbound);
        outboundDTO.setOutboundCode(newOutboundCode);

        Product product = productRepository.findByProductCode(outboundDTO.getProductCode())
                .orElseThrow(() -> new RuntimeException("Product not found with productCode: " + outboundDTO.getProductCode()));

        Supplier supplier = supplierRepository.findBySupplierId(outboundDTO.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found with supplierId: " + outboundDTO.getSupplierId()));

        Outbound outbound = modelMapper.map(outboundDTO, Outbound.class);
        outbound.setProduct(product);
        outbound.setSupplier(supplier);

        return outboundRepository.save(outbound).getOutboundId();
    }

    // inboundCode 생성 로직
    private String generateNewOutboundCode(Optional<Outbound> lastOutbound) {
        String newOutboundCode = "O001";

        if (lastOutbound.isPresent()) {
            String lastOutboundCode = lastOutbound.get().getOutboundCode();
            int lastCodeNumber = Integer.parseInt(lastOutboundCode.substring(1)); // "i001" -> 1
            newOutboundCode = "O" + String.format("%03d", lastCodeNumber + 1); // "i002", "i003", ...
        }

        return newOutboundCode;
    }

    @Override
    public OutboundDTO readOne(Long outboundId){
        Optional<Outbound> result = outboundRepository.findById(outboundId);
        Outbound outbound = result.orElseThrow(() -> new RuntimeException("Outbound not found"));
        OutboundDTO outboundDTO = modelMapper.map(outbound, OutboundDTO.class);

        if (outbound.getProduct() != null) {
            outboundDTO.setProductCode(outbound.getProduct().getProductCode());
        }
        if (outbound.getSupplier() != null) {
            outboundDTO.setSupplierId(outbound.getSupplier().getSupplierId());
        }

        return outboundDTO;
    }

    @Override
    public void modify(OutboundDTO outboundDTO){
        Optional<Outbound> result = outboundRepository.findById(outboundDTO.getOutboundId());

        Outbound outbound = result.orElseThrow(() -> new RuntimeException("Outbound not found"));

        Product newProduct = productRepository.findByProductCode(outboundDTO.getProductCode())
                .orElseThrow(() -> new RuntimeException("Product not found with productCode"));
        Supplier newSupplier = supplierRepository.findBySupplierId(outboundDTO.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found with supplierId"));

        outbound.change(newProduct,
                outboundDTO.getOutboundCode(),
                newSupplier,
                outboundDTO.getQuantity(),
                outboundDTO.getOutboundDate(),
                outboundDTO.getDescription(),
                outboundDTO.getOutboundStatus());

        outboundRepository.save(outbound);
    }

    @Override
    public void remove(Long inboundId){
        outboundRepository.deleteById(inboundId);
    }

    @Override
    public PageResponseDTO list(PageRequestDTO pageRequestDTO){
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("inboundId");

        Page<Outbound> result = outboundRepository.searchAll(types,keyword, pageable);

        List<OutboundDTO> dtoList = result.getContent().stream().map(outbound -> {
            OutboundDTO outboundDTO = modelMapper.map(outbound, OutboundDTO.class);

            // Product 엔티티에서 productCode 설정
            if (outbound.getProduct() != null) {
                outboundDTO.setProductCode(outbound.getProduct().getProductCode());
            }
            // Product 엔티티에서 supplierId 설정
            if (outbound.getSupplier() != null) {
                outboundDTO.setSupplierId(outbound.getSupplier().getSupplierId());
            }

            return outboundDTO;
        }).collect(Collectors.toList());

        return PageResponseDTO.<OutboundDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public List<OutboundDTO> getAllOutbound() {
        List<Outbound> outbounds = outboundRepository.findAll();  // 전체 데이터 가져오기

        log.info("data" + outbounds.size());

        return outbounds.stream()
                .map(outbound -> modelMapper.map(outbound, OutboundDTO.class))
                .collect(Collectors.toList());
    }
}
