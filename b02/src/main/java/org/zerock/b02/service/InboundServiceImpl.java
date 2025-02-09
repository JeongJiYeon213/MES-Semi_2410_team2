package org.zerock.b02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.b02.domain.Inbound;
import org.zerock.b02.dto.InboundDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.repository.InboundRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class InboundServiceImpl implements InboundService{
    private final ModelMapper modelMapper;
    private final InboundRepository inboundRepository;

    @Override
    public Long register(InboundDTO inboundDTO){
        Inbound inbound = modelMapper.map(inboundDTO, Inbound.class);
        Long inboundId = inboundRepository.save(inbound).getInboundId();
        return inboundId;
    }
    @Override
    public InboundDTO readOne(Long inboundId){
        Optional<Inbound> result = inboundRepository.findById(inboundId);
        Inbound inbound = result.orElseThrow();
        InboundDTO inboundDTO = modelMapper.map(inbound, InboundDTO.class);
        return inboundDTO;
    }

    @Override
    public void modify(InboundDTO inboundDTO){
        Optional<Inbound> result = inboundRepository.findById(inboundDTO.getInboundId());
        Inbound inbound = result.orElseThrow();
        inbound.change(inboundDTO.getProductCode(),
                inboundDTO.getInboundCode(),
                inboundDTO.getSupplierId(),
                inboundDTO.getQuantity(),
                inboundDTO.getInboundDate(),
                inboundDTO.getDescription(),
                inboundDTO.getInboundStatus());

        inboundRepository.save(inbound);
    }

    @Override
    public void remove(Long inboundId){
        inboundRepository.deleteById(inboundId);
    }

    @Override
    public PageResponseDTO list(PageRequestDTO pageRequestDTO){
        // 브라우저에서 요청한 파라미터 값 세팅
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("inboundId");

        // 브라우저에서 받은 파라미터로 board테이블 sql 작성 page<board> 객체로 전달
        Page<Inbound> result = inboundRepository.searchAll(types,keyword, pageable);

        // modelMapper를 통해서 entitu -> dto로 변환
        List<InboundDTO> dtoList = result.getContent().stream()
                .map(inbound -> modelMapper.map(inbound, InboundDTO.class)).collect(Collectors.toList());

        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }
}
