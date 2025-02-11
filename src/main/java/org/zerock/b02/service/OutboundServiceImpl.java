package org.zerock.b02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.b02.domain.Outbound;
import org.zerock.b02.dto.OutboundDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.repository.OutboundRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class OutboundServiceImpl implements OutboundService{
    private final ModelMapper modelMapper;
    private final OutboundRepository outboundRepository;

    @Override
    public Long register(OutboundDTO outboundDTO){
        Optional<Outbound> lastOutbound = outboundRepository.findTopByOrderByOutboundCodeDesc();

        String newOutboundCode = generateNewOutboundCode(lastOutbound);
        outboundDTO.setOutboundCode(newOutboundCode);

        Outbound outbound = modelMapper.map(outboundDTO, Outbound.class);
        return outboundRepository.save(outbound).getOutboundId();
    }

    // inboundCode 생성 로직
    private String generateNewOutboundCode(Optional<Outbound> lastOutbound) {
        String newOutboundCode = "o001";

        if (lastOutbound.isPresent()) {
            String lastOutboundCode = lastOutbound.get().getOutboundCode();
            int lastCodeNumber = Integer.parseInt(lastOutboundCode.substring(1)); // "i001" -> 1
            newOutboundCode = "o" + String.format("%03d", lastCodeNumber + 1); // "i002", "i003", ...
        }

        return newOutboundCode;
    }

    @Override
    public OutboundDTO readOne(Long outboundId){
        Optional<Outbound> result = outboundRepository.findById(outboundId);
        Outbound outbound = result.orElseThrow(() -> new RuntimeException("Outbound not found"));
        return modelMapper.map(outbound, OutboundDTO.class);
    }

    @Override
    public void modify(OutboundDTO outboundDTO){
        Optional<Outbound> result = outboundRepository.findById(outboundDTO.getOutboundId());
        Outbound outbound = result.orElseThrow();
        outbound.change(outboundDTO.getProductCode(),
                outboundDTO.getOutboundCode(),
                outboundDTO.getSupplierId(),
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

        List<OutboundDTO> dtoList = result.getContent().stream()
                .map(outbound -> modelMapper.map(outbound, OutboundDTO.class)).collect(Collectors.toList());

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
